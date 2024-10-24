package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO class for the database operations regarding the uploads
 */
public class UserUploadDAO extends BaseDAO{
    private Connection connection;

    public UserUploadDAO () {
        connection = DatabaseConnection.getInstance();
        createUploadTable();
    }

    /**
     * Creates the uploads table if not exists
     */
    public void createUploadTable() {
        String tableSQL = "CREATE TABLE IF NOT EXISTS userUploads ("
                + "upload_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "upload_name TEXT NOT NULL, "
                + "upload_categories TEXT, "
                + "upload_type TEXT, "
                + "upload_description TEXT, "
                + "is_pinned BOOLEAN NOT NULL DEFAULT 0, "
                + "image_data TEXT, "
                + "uploaded_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime')), "
                + "FOREIGN KEY (user_id) REFERENCES userAccounts(user_id))";

        // Use BaseDAO's createTable method
        createTable("userUploads", tableSQL);
    }


    /**
     * Method to check if a user with the given user ID exists
     * @param userId The user ID to be checked
     * @return Returns false if there's an exception or if no user is found, true otherwise
     */
    public boolean userExists(int userId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT COUNT(*) FROM userAccounts WHERE user_id = ?"
            );
            preparedStatement.setInt(1, userId);
            ResultSet rs = preparedStatement.executeQuery();
            return rs.getInt(1) > 0;
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false;
        }
    }

    /**
     * Method for getting a user ID given a upload ID
     * @param uploadId The upload ID to be searched
     * @return Returns the user ID matching the given upload ID
     */


    public int getUserIdByUploadId(int uploadId) {
        // Reuse the getSomeIntByInt method from BaseDAO
        return getSomeIntByInt("userUploads", "user_id", "upload_id", uploadId);
    }


    /**
     * Method for creating a new upload
     * @param userUpload The upload details
     */
    public void newUpload(UserUpload userUpload) {
        if (userExists(userUpload.getUserId()))
        {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO userUploads (user_id, upload_name, upload_categories, upload_type, upload_description, is_pinned, uploaded_at, image_data) "
                                + "VALUES (?, ?, ?, ?, ?, ?, datetime('now', 'localtime'), ?)"
                );
                preparedStatement.setInt(1, userUpload.getUserId());
                preparedStatement.setString(2, userUpload.getUploadName());
                preparedStatement.setString(3, userUpload.getUploadCategories());
                preparedStatement.setString(4, userUpload.getUploadType());
                preparedStatement.setString(5, userUpload.getUploadDescription());
                preparedStatement.setBoolean(6, userUpload.isPinned());
                preparedStatement.setString(7, userUpload.getImageData());

                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error inserting upload: " + e.getMessage());
            }
        } else {
            System.err.println("User with ID " + userUpload.getUserId() + " does not exist.");
        }
    }

    /**
     * Method for retuning all uploads in the database
     * @return Returns all user uploads on the database
     */
    public List<UserUpload> allUploads() {
        List<UserUpload> uploads = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM userUploads");

            while (rs.next()) {
                uploads.add(new UserUpload(
                        rs.getInt("upload_id"),
                        rs.getInt("user_id"),
                        rs.getString("upload_name"),
                        rs.getString("upload_categories"),
                        rs.getString("upload_type"),
                        rs.getString("upload_description"),
                        rs.getBoolean("is_pinned"),
                        rs.getString("image_data"),
                        rs.getString("uploaded_at")
                ));
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return uploads;
    }

    /**
     * Method to get the upload details given the upload ID
     * @param upload_id The upload ID to be searched
     * @return Returns the upload matching the upload ID, null otherwise
     */
    public UserUpload getUploadById(int upload_id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(
                    "SELECT * FROM userUploads WHERE upload_id = ?"
            );
            preparedStatement.setInt(1, upload_id);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                return new UserUpload(
                        rs.getInt("upload_id"),
                        rs.getInt("user_id"),
                        rs.getString("upload_name"),
                        rs.getString("upload_categories"),
                        rs.getString("upload_type"),
                        rs.getString("upload_description"),
                        rs.getBoolean("is_pinned"),
                        rs.getString("image_data"),
                        rs.getString("uploaded_at")
                );
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return null;
    }
}

package com.example.local_history_archive.model;

import com.example.local_history_archive.controller.UserUpload;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserUploadDAO {
    private Connection connection;

    public UserUploadDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createUploadTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute("PRAGMA foreign_keys = ON");
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS userUploads ("
                            + "upload_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "user_id INTEGER NOT NULL, "
                            + "upload_name TEXT NOT NULL, "
                            + "upload_categories TEXT, "
                            + "upload_type TEXT, "
                            + "upload_description TEXT, "
                            + "is_pinned BOOLEAN NOT NULL DEFAULT 0, "
                            + "image_data TEXT, "
                            + "uploaded_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime')), "
                            + "FOREIGN KEY (user_id) REFERENCES userAccounts(user_id)"
                            + ")"
            );
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

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

    public int getUserIdByUploadId(int uploadId) {
        int userId = -1;
        String query = "SELECT user_id FROM userUploads WHERE upload_id = ?";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, uploadId);
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("user_id");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user_id: " + e.getMessage());
        }

        return userId;
    }


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

    public void updateUpload(UserUpload userUpload) {
        if (userExists(userUpload.getUserId())) {
            try {
                PreparedStatement updateUploadStmt = connection.prepareStatement(
                        "UPDATE userUploads SET upload_name = ?, upload_categories = ?, upload_type = ?, upload_description = ?, is_pinned = ?, image_data = ? "
                                + "WHERE upload_id = ?"
                );
                updateUploadStmt.setString(1, userUpload.getUploadName());
                updateUploadStmt.setString(2, userUpload.getUploadCategories());
                updateUploadStmt.setString(3, userUpload.getUploadType());
                updateUploadStmt.setString(4, userUpload.getUploadDescription());
                updateUploadStmt.setBoolean(5, userUpload.isPinned());
                updateUploadStmt.setString(6, userUpload.getImageData());
                updateUploadStmt.setInt(7, userUpload.getUploadId());

                updateUploadStmt.executeUpdate();
            } catch (SQLException SQLEx) {
                System.err.println(SQLEx);
            }
        } else {
            System.err.println("User with ID " + userUpload.getUserId() + " does not exist.");
        }
    }

    public void deleteUpload(int upload_id) {

        int userId = getUserIdByUploadId(upload_id);

        if (userId != -1) {
        try {
            PreparedStatement deleteUploadStmt = connection.prepareStatement(
                    "DELETE FROM userUploads WHERE upload_id = ?"
            );
            deleteUploadStmt.setInt(1, upload_id);
            deleteUploadStmt.executeUpdate();
            System.out.println("Upload with ID " + upload_id + " has been deleted.");
        } catch (SQLException SQLEx) {
            System.err.println("Error deleting upload: " + SQLEx.getMessage());
        }
    } else {
        System.err.println("Upload with ID " + upload_id + " does not exist or has no associated user.");
    }
    }

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
                        rs.getString("image_data")
                ));
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return uploads;
    }

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
                        rs.getString("image_data")
                );
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return null;
    }

    public void dropTable() throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.execute("DROP TABLE IF EXISTS userUploads");
            System.out.println("Table userUploads deleted successfully.");
        } catch (SQLException e) {
            System.err.println("Error deleting table: " + e.getMessage());
        } finally {
            try {
                statement.close();
            } catch (SQLException e) {
                System.err.println("Error closing statement: " + e.getMessage());
            }
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }
}

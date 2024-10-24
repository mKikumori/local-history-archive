package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO class for the database operations regarding the collections
 */
public class CollectionDAO extends BaseDAO{
    private Connection connection;

    public CollectionDAO () {
        connection = DatabaseConnection.getInstance();
        createCollectionTable();
        createCollectionUploadsTable();

    }

    /**
     * Creates the collections table if not exists
     */

    public void createCollectionTable() {
        String tableSQL = "CREATE TABLE IF NOT EXISTS Collections ("
                + "collection_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "creator_id INTEGER, "
                + "collection_name TEXT NOT NULL, "
                + "created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime')), "
                + "FOREIGN KEY (creator_id) REFERENCES userAccounts(user_id))";

        // Call the method from BaseDAO and pass the table name and SQL statement
        createTable("Collections", tableSQL);
    }

    /**
     * Creates the linking table for collections and uploads if not exists
     */
    public void createCollectionUploadsTable() {
        String tableSQL = "CREATE TABLE IF NOT EXISTS CollectionUploads ("
                + "collection_id INTEGER NOT NULL, "
                + "upload_id INTEGER NOT NULL, "
                + "PRIMARY KEY (collection_id, upload_id), "
                + "FOREIGN KEY (collection_id) REFERENCES Collections(collection_id), "
                + "FOREIGN KEY (upload_id) REFERENCES userUploads(upload_id))";
        createTable("CollectionUploads", tableSQL);
    }
    /**
     * Method for creating a new collection
     * @param collection The param for getting the details of the collection name and the creator's ID
     */
    public void newCollection(Collection collection) {
        try (PreparedStatement newCollection = connection.prepareStatement(
                "INSERT INTO Collections (creator_id, collection_name, created_at) "
                        + "VALUES (?, ?, datetime('now', 'localtime'))")) {
            newCollection.setInt(1, collection.getCreator_id());
            newCollection.setString(2, collection.getCollection_name());
            newCollection.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    /**
     * Gets the collection name by the user's ID
     * @param userId The user's ID
     * @return Returns the name of the collection
     */
    public String getCollectionNameByUserId(int userId) {
        String collectionName = null;
        String query = "SELECT collection_name FROM Collections WHERE creator_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                collectionName = rs.getString("collection_name");
            }
        } catch (SQLException SQLEx) {
            System.err.println("Error fetching collection name for user: " + SQLEx.getMessage());
        }

        return collectionName;
    }

    /**
     * Checks if a user already has a collection
     * @param userId The user's ID
     * @return Returns false if there's an exception or if no collection is found. Returns true otherwise
     */
    public boolean userHasCollection(int userId) {
        String query = "SELECT COUNT(*) FROM Collections WHERE creator_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException SQLEx) {
            System.err.println("Error checking if user has a collection: " + SQLEx);
        }
        return false;
    }

    /**
     * Method to add an upload to a collection based on the creator_id, as only the creator can add more uploads to the collection
     * @param creatorId The ID of the creator of the collection
     * @param uploadId The ID of the upload to be added
     */
    public void addUploadToCollection(int creatorId, int uploadId) {
        try (PreparedStatement addUpload = connection.prepareStatement(
                "INSERT INTO CollectionUploads (collection_id, upload_id) " +
                        "SELECT collection_id, ? FROM Collections WHERE creator_id = ?")) {
            addUpload.setInt(1, uploadId);
            addUpload.setInt(2, creatorId);
            addUpload.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println("Error adding upload to collection: " + SQLEx);
        }
    }

    /**
     * Method to get all uploads in a collection for a given user ID
     * @param userId The ID of the current user
     * @return All uploads found in the collection for that user ID
     */
    public List<UserUpload> getUploadsForUser(int userId) {
        List<UserUpload> uploads = new ArrayList<>();
        String query = "SELECT u.* FROM userUploads u " +
                "INNER JOIN CollectionUploads cu ON u.upload_id = cu.upload_id " +
                "INNER JOIN Collections c ON c.collection_id = cu.collection_id " +
                "WHERE c.creator_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                UserUpload upload = new UserUpload(
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
                uploads.add(upload);
            }
        } catch (SQLException SQLEx) {
            System.err.println("Error fetching uploads for user: " + SQLEx);
        }
        return uploads;
    }

    /**
     * Method to delete an upload by its creator ID, as only the creator can delete it
     * @param uploadId The upload ID to be deleted
     * @param userId The user ID of the creator
     * @return Returns false if there was an error. True otherwise
     */
    public boolean deleteUploadByCreator(int uploadId, int userId) {
        boolean isDeleted = false;

        String checkQuery = "SELECT user_id FROM userUploads WHERE upload_id = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
            checkStmt.setInt(1, uploadId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                int creatorId = rs.getInt("user_id");

                if (creatorId == userId) {
                    String deleteFromCollectionUploads = "DELETE FROM CollectionUploads WHERE upload_id = ?";
                    try (PreparedStatement deleteFromCollectionStmt = connection.prepareStatement(deleteFromCollectionUploads)) {
                        deleteFromCollectionStmt.setInt(1, uploadId);
                        deleteFromCollectionStmt.executeUpdate();
                    }

                    String deleteUpload = "DELETE FROM userUploads WHERE upload_id = ?";
                    try (PreparedStatement deleteUploadStmt = connection.prepareStatement(deleteUpload)) {
                        deleteUploadStmt.setInt(1, uploadId);
                        int rowsAffected = deleteUploadStmt.executeUpdate();
                        isDeleted = (rowsAffected > 0);
                    }
                } else {
                    System.err.println("Error: User is not the creator of the upload.");
                }
            } else {
                System.err.println("Error: Upload not found.");
            }

        } catch (SQLException SQLEx) {
            System.err.println("Error deleting upload: " + SQLEx.getMessage());
        }
        return isDeleted;
    }

    /**
     * Method to remove an upload from a collection without deleting the upload itself
     * @param uploadId The upload ID to be deleted
     * @param userId The user ID of the creator
     * @return Returns false if there is an error, true otherwise
     */
    public boolean removeUploadFromCollection(int uploadId, int userId) {
        boolean isRemoved = false;

        String verifyQuery = "SELECT collection_id FROM Collections WHERE creator_id = ?";

        try (PreparedStatement verifyStmt = connection.prepareStatement(verifyQuery)) {
            verifyStmt.setInt(1, userId);
            ResultSet rs = verifyStmt.executeQuery();

            if (rs.next()) {
                int collectionId = rs.getInt("collection_id");

                String removeQuery = "DELETE FROM CollectionUploads WHERE collection_id = ? AND upload_id = ?";
                try (PreparedStatement removeStmt = connection.prepareStatement(removeQuery)) {
                    removeStmt.setInt(1, collectionId);
                    removeStmt.setInt(2, uploadId);
                    int rowsAffected = removeStmt.executeUpdate();
                    isRemoved = (rowsAffected > 0);
                }
            } else {
                System.err.println("Error: Collection not found for the user.");
            }

        } catch (SQLException SQLEx) {
            System.err.println("Error removing upload from collection: " + SQLEx.getMessage());
        }

        return isRemoved;
    }
}
package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionDAO {
    private Connection connection;

    public CollectionDAO() {
        connection = DatabaseConnection.getInstance();
        createCollectionTable();
        createCollectionUploadsTable();
    }

    public void createCollectionTable() {
        try (Statement createTable = connection.createStatement()) {
            createTable.execute("PRAGMA foreign_keys = ON");
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS Collections ("
                            + "collection_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "creator_id INTEGER, "
                            + "collection_name TEXT NOT NULL, "
                            + "created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime')), "
                            + "FOREIGN KEY (creator_id) REFERENCES userAccounts(user_id))"
            );
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    // Method to create the linking table for collections and uploads
    public void createCollectionUploadsTable() {
        try (Statement createTable = connection.createStatement()) {
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS CollectionUploads ("
                            + "collection_id INTEGER NOT NULL, "
                            + "upload_id INTEGER NOT NULL, "
                            + "PRIMARY KEY (collection_id, upload_id), "
                            + "FOREIGN KEY (collection_id) REFERENCES Collections(collection_id), "
                            + "FOREIGN KEY (upload_id) REFERENCES userUploads(upload_id))"
            );
        } catch (SQLException SQLEx) {
            System.err.println("Error creating CollectionUploads table: " + SQLEx.getMessage());
        }
    }

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

    // Method to check if a user has a collection
    public boolean userHasCollection(int userId) {
        String query = "SELECT COUNT(*) FROM Collections WHERE creator_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0; // Returns true if the count is greater than 0
            }
        } catch (SQLException SQLEx) {
            System.err.println("Error checking if user has a collection: " + SQLEx);
        }

        return false; // Default return false if there's an exception or no collection found
    }

    // Method to add an upload to a collection based on creator_id
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

    // Method to get all uploads for a given userId (creatorId)
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
}
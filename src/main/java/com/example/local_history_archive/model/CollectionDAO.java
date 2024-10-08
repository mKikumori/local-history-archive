package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionDAO {
    private Connection connection;

    public CollectionDAO() {
        connection = DatabaseConnection.getInstance();
    }

    public void createCollectionTable() {
        try (Statement createTable = connection.createStatement()) {
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS Collections ("
                            + "collection_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "creator_id INTEGER, "
                            + "upload_id INTEGER, "
                            + "collection_name TEXT NOT NULL, "
                            + "category TEXT"
                            + "shared_with TEXT, "
                            + "created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime')), "
                            + "FOREIGN KEY (creator_id) REFERENCES Users(user_id), "
                            + "FOREIGN KEY (upload_id) REFERENCES Uploads(upload_id))"
            );
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void assignCategoryToCollection(int collection_id, String category) {
        try (PreparedStatement assignCategory = connection.prepareStatement(
                "UPDATE Collections SET category = ? WHERE collection_id = ?")) {
            assignCategory.setString(1, category);
            assignCategory.setInt(2, collection_id);
            assignCategory.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }


    public List<Collection> getAll() {
        List<Collection> collections = new ArrayList<>();
        try (Statement getAll = connection.createStatement();
             ResultSet rs = getAll.executeQuery("SELECT * FROM Collections")) {

            while (rs.next()) {
                collections.add(
                        new Collection(
                                rs.getInt("collection_id"),
                                rs.getString("collection_name")
                        )
                );
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return collections;
    }

    public void newCollection(Collection collection) {
        try (PreparedStatement newCollection = connection.prepareStatement(
                "INSERT INTO Collections (creator_id, upload_id, collection_name, shared_with, created_at) "
                        + "VALUES (?, ?, ?, ?, datetime('now', 'localtime'))")) {
            newCollection.setInt(1, collection.getCreator_id());
            newCollection.setInt(2, collection.getUpload_id());
            newCollection.setString(3, collection.getCollection_name());
            newCollection.setInt(4, collection.getShared_with());

            newCollection.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void updateCollection(Collection collection) {
        try (PreparedStatement updateCollection = connection.prepareStatement(
                "UPDATE Collections SET collection_name = ?, shared_with = ? WHERE collection_id = ?")) {
            updateCollection.setString(1, collection.getCollection_name());
            updateCollection.setInt(2, collection.getShared_with());
            updateCollection.setInt(3, collection.getCollection_id());

            updateCollection.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void deleteCollection(int collection_id) {
        try (PreparedStatement deleteCollection = connection.prepareStatement(
                "DELETE FROM Collections WHERE collection_id = ?")) {
            deleteCollection.setInt(1, collection_id);
            deleteCollection.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void makeCollectionPublic(int collection_id) {
        try (PreparedStatement makePublic = connection.prepareStatement(
                "UPDATE Collections SET shared_with = '-1' WHERE collection_id = ?")) {
            makePublic.setInt(1, collection_id);
            makePublic.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void shareCollectionWithUsers(int collection_id, List<Integer> user_ids) {
        try {
            // Prepare the shared_with string from the list of user IDs
            StringBuilder sharedWithBuilder = new StringBuilder();

            for (int i = 0; i < user_ids.size(); i++) {
                sharedWithBuilder.append(user_ids.get(i));
                if (i < user_ids.size() - 1) {
                    sharedWithBuilder.append(",");
                }
            }

            String sharedWith = sharedWithBuilder.toString();

            // Update the collection with the shared_with field
            try (PreparedStatement shareWithUsers = connection.prepareStatement(
                    "UPDATE Collections SET shared_with = ? WHERE collection_id = ?")) {
                shareWithUsers.setString(1, sharedWith);
                shareWithUsers.setInt(2, collection_id);
                shareWithUsers.executeUpdate();
            }

        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }
}
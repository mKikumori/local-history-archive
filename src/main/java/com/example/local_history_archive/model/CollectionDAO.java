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
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS Collections ("
                            + "collection_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                            + "creator_id INTEGER FOREIGN KEY,"
                            + "upload_id INTEGER FOREIGN KEY,"
                            + "collection_name TEXT NOT NULL,"
                            + "shared_with TEXT,"
                            + "created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))"
                            + ")"
            );
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }
    public List<Collection> getAll() {
        List<Collection> collections = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM Collections");

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

    public void newCollection(Collection Collection) {
        try {
            PreparedStatement newCollection = connection.prepareStatement(
                    "INSERT INTO Collections (collection_id, creator_id, upload_id, collection_name, shared_with, created_at) "
                            + "VALUES (?, ?, ?, ?, ?, datetime('now', 'localtime'))"
            );
            newCollection.setInt(1, Collection.getCollection_id());
            newCollection.setInt(2, Collection.getCreator_id());
            newCollection.setInt(3, Collection.getUpload_id());
            newCollection.setString(4, Collection.getCollection_name());
            newCollection.setInt(5, Collection.getShared_with());
            newCollection.setString(6, Collection.getCreated_at());

        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void updateCollection(Collection Collection) {
        try {
            PreparedStatement updateCollection = connection.prepareStatement(
                    "UPDATE Collections SET collection_name = ?, shared_with = ?"
            );
            updateCollection.setString(1, Collection.getCollection_name());
            updateCollection.setInt(2, Collection.getShared_with());

            updateCollection.execute();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void deleteCollection(int collection_id) {
        try{
            PreparedStatement deleteCollection = connection.prepareStatement("DELETE FROM Collections WHERE Collection_id = ?");
            deleteCollection.setInt(1, collection_id);
            deleteCollection.execute();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void makeCollectionPublic(int collection_id) {
        try {
            PreparedStatement makePublic = connection.prepareStatement(
                    "UPDATE Collections SET shared_with = '-1' WHERE collection_id = ?"
            );
            makePublic.setInt(1, collection_id);
            makePublic.executeUpdate();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }
    public void shareCollectionWithUsers(int collection_id, List<Integer> user_ids) {
        try {
            // Create an instance of UserAccountDAO to access getById
            UserAccountDAO userAccountDAO = new UserAccountDAO();

            // Prepare the shared_with string from the list of user IDs
            StringBuilder sharedWithBuilder = new StringBuilder();

            for (int i = 0; i < user_ids.size(); i++) {
                int user_id = user_ids.get(i);

                // Call getById to ensure the user exists
                UserAccount userAccount = userAccountDAO.getById(user_id);
                if (userAccount != null) {
                    sharedWithBuilder.append(user_id);
                    if (i < user_ids.size() - 1) {
                        sharedWithBuilder.append(",");
                    }
                } else {
                    System.err.println("User with ID " + user_id + " does not exist.");
                }
            }

            String sharedWith = sharedWithBuilder.toString();

            // Update the collection with the shared_with field
            PreparedStatement shareWithUsers = connection.prepareStatement(
                    "UPDATE Collections SET shared_with = ? WHERE collection_id = ?"
            );
            shareWithUsers.setString(1, sharedWith);
            shareWithUsers.setInt(2, collection_id);
            shareWithUsers.executeUpdate();

        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

}
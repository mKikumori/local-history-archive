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
                            + "shared_with INTEGER,"
                            + "created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))"
                            + ")"
            );
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
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
}
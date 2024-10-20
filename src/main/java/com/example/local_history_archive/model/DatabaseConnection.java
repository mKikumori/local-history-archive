package com.example.local_history_archive.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A simple class for establishing a connection with the sqlite database
 */
public class DatabaseConnection {
    private static Connection instance = null;

    private DatabaseConnection() {
        String url = "jdbc:sqlite:database.db";

        try {
            instance = DriverManager.getConnection(url);
            Statement statement = instance.createStatement();
            statement.execute("PRAGMA foreign_keys = ON");
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx);
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection();
        }
        return instance;
    }
}

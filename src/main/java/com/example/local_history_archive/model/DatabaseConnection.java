package com.example.local_history_archive.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static Connection instance = null;

    private DatabaseConnection() {
        String url = "jdbc:sqlite:database.db";

        try {
            instance = DriverManager.getConnection(url); // This tries to connect using the URL but it fails due to smt idk, maybe the "org.xerial:sqlite-jdbc:3.36.0.3"
        } catch (SQLException sqlEx) {
            System.err.println(sqlEx); // This is giving out the "java.sql.SQLException: No suitable driver found for jdbc:sqlite:database.db" error
        }
    }

    public static Connection getInstance() {
        if (instance == null) {
            new DatabaseConnection(); // The DAOs call this function and instance is always null
                                        // Because of that DatabaseConnection() is called
        }
        return instance;
    }
}

package com.example.local_history_archive.model;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


import java.sql.*;

public class BaseDAO {

    private Connection connection;

    public BaseDao ()
    {
        connection = DatabaseConnection.getInstance();
    }

    protected void createTable(String tableName, String tableSQL) {
        try (Statement createTable = connection.createStatement()) {
            createTable.execute("PRAGMA foreign_keys = ON");  // Enable foreign keys
            createTable.execute(tableSQL);  // Execute the table creation SQL
            System.out.println("Table " + tableName + " created or already exists.");
        } catch (SQLException SQLEx) {
            System.err.println("Error creating table " + tableName + ": " + SQLEx.getMessage());
        }
    }

    protected String getFieldValueByCondition(String tableName, String fieldName, String conditionField, int conditionValue) {
        String fieldValue = null;
        String query = "SELECT " + fieldName + " FROM " + tableName + " WHERE " + conditionField + " = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, conditionValue);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                fieldValue = rs.getString(fieldName);
            }
        } catch (SQLException SQLEx) {
            System.err.println("Error fetching field " + fieldName + " for condition: " + SQLEx.getMessage());
        }

        return fieldValue;
    }

    protected String getSomeStringByInt(String tableName, String columnName, int anyInt) {
        String result = null;
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE creator_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, anyInt);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result = rs.getString(columnName);
            }
        } catch (SQLException SQLEx) {
            System.err.println("Error fetching " + columnName + " from " + tableName + ": " + SQLEx.getMessage());
        }

        return result;
    }

    protected int getSomeIntByInt(String tableName, String columnName, String conditionField, int conditionValue) {
        int result = 0;
        String query = "SELECT " + columnName + " FROM " + tableName + " WHERE creator_id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, conditionValue);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(columnName);
            }
        } catch (SQLException SQLEx) {
            System.err.println("Error fetching " + columnName + " from " + tableName + ": " + SQLEx.getMessage());
        }

        return result;
    }

}

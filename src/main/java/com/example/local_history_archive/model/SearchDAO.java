package com.example.local_history_archive.model;

import com.example.local_history_archive.controller.SearchResult;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchDAO {
    private Connection connection;

    // Constructor to initialize the connection
    public SearchDAO(Connection connection) {
        this.connection = connection;
    }

    /**
     * Searches both Users and Collections tables based on a given search query.
     * @param searchQuery the query to match against usernames and collection names
     * @return a list of search results encapsulating matched records
     */
    public List<SearchResult> searchAcrossTables(String searchQuery) {
        List<SearchResult> results = new ArrayList<>();
        String sql = "SELECT 'User' AS type, user_id AS id, username AS result FROM Users WHERE username LIKE ? " +
                "UNION ALL " +
                "SELECT 'Collection' AS type, collection_id AS id, collection_name AS result FROM Collections WHERE collection_name LIKE ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchQuery + "%");  // Set parameter for Users table
            pstmt.setString(2, "%" + searchQuery + "%");  // Set parameter for Collections table

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                results.add(new SearchResult(
                        rs.getString("type"),
                        rs.getInt("id"),
                        rs.getString("result")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error during search operation: " + e.getMessage());
        }
        return results;
    }

    // Additional methods if necessary...
}
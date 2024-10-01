package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SearchDAO {
    private Connection connection;
    public SearchDAO(Connection connection) {
        this.connection = connection;
    }

    public List<SearchResult> searchAcrossTables(String searchQuery) {
        List<SearchResult> results = new ArrayList<>();
        searchQuery = "SELECT 'User' AS type, user_id AS id, username AS result FROM Users WHERE username LIKE ? " +
                "UNION ALL " +
                "SELECT 'Collection' AS type, collection_id AS id, collection_name AS result FROM Collections WHERE collection_name LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(searchQuery)) {
            preparedStatement.setString(1, "%" + searchQuery + "%");
            preparedStatement.setString(2, "%" + searchQuery + "%");
            ResultSet rs = preparedStatement.executeQuery();
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

    public List<SearchResult> searchByCategory(String category) {
        List<SearchResult> results = new ArrayList<>();
        String query = "SELECT 'Collection' AS type, collection_id AS id, collection_name AS result FROM Collections WHERE upload_category LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + category + "%");
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                results.add(new SearchResult(
                        rs.getString("type"),
                        rs.getInt("id"),
                        rs.getString("result")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error during search by category operation: " + e.getMessage());
        }
        return results;
    }

}
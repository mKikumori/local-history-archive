package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchDAO {
    private Connection connection;

    public SearchDAO() {
        connection = DatabaseConnection.getInstance();
    }

    // Search uploads by title (upload_name)
    public List<SearchResult> searchUploadsByTitle(String title) {
        List<SearchResult> results = new ArrayList<>();
        String query = "SELECT upload_id, upload_name, upload_description, upload_categories, upload_type, uploaded_at, image_data " +
                "FROM userUploads WHERE upload_name LIKE ?";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + title + "%");  // Add wildcards for partial match on title
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                // Create a list of result details to store all the necessary fields
                List<String> resultData = Arrays.asList(
                        rs.getString("upload_name"),
                        rs.getString("upload_description"),
                        rs.getString("upload_categories"),
                        rs.getString("uploaded_at") // Exclude upload_type here, we'll add it to SearchResult directly
                );
                // Add the SearchResult object containing the upload data, image data, and upload type
                results.add(new SearchResult("Upload", rs.getInt("upload_id"), resultData, rs.getString("image_data"), rs.getString("upload_type")));
            }
        } catch (SQLException e) {
            System.err.println("Error during search by upload title operation: " + e.getMessage());
        }
        return results;
    }

}

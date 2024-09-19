package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;

public class SearchController {

    @FXML
    private TextField searchField; // TextField for entering search query
    @FXML
    private Button searchBtn; // Button to initiate search
    @FXML
    private GridPane resultsGrid; // Grid to display search results

    private SearchDAO searchDAO;


    @FXML
    private void onSearchClicked() throws IOException {
        String query = searchField.getText().trim();
        if (query.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Search Error", "Please enter a search query.");
            return;
        }
        List<SearchResult> results = searchDAO.searchAcrossTables(query);
        displaySearchResults(results);
    }

    private void displaySearchResults(List<SearchResult> results) {
        resultsGrid.getChildren().clear(); // Clear previous results
        if (results.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No results found for your search.");
        } else {
            int row = 0, column = 0;
            for (SearchResult result : results) {
                Button resultButton = new Button(result.getResult());
                resultButton.setOnAction(event -> {
                    try {
                        showDetail(result);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
                resultsGrid.add(resultButton, column++, row);
                if (column > 2) {
                    column = 0;
                    row++;
                }
            }
        }
    }

    private void showDetail(SearchResult result) throws IOException {
        showAlert(Alert.AlertType.INFORMATION, "Result", "Detail for: " + result.getResult());
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
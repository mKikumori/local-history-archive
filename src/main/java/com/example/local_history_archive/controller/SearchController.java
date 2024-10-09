package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;

public class SearchController {
    @FXML
    public Button homeBtn;
    @FXML
    public Button collectionBtn;
    @FXML
    public Button settingsBtn;
    @FXML
    public Button profileBtn;
    @FXML
    public Button searchBtn;
    @FXML
    public Label searchQuery;
    @FXML
    private Button uploadBtn;
    @FXML
    private TextField searchField;
    @FXML
    private GridPane resultsGrid;

    private SearchDAO searchDAO;

    @FXML
    public void initialize() {
        if (searchDAO == null) {
            searchDAO = new SearchDAO();
        }
    }

    @FXML
    private void onSearchClicked() throws IOException {
        UserAccount currentUser = SessionManager.getCurrentUser();  // Retrieve the current logged-in user

        if (currentUser != null) {
            String query = searchField.getText().trim();
            searchQuery.setText(query);

            if (query.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Query", "Please enter a search query.");
                return;
            }

            // Use 'userId' to perform the search
            List<SearchResult> results = searchDAO.searchAcrossTables(query);
            displaySearchResults(results);

        } else {
            showAlert(Alert.AlertType.ERROR, "Search Error", "Please log in first.");
        }
    }

    private void displaySearchResults(List<SearchResult> results) {
        resultsGrid.getChildren().clear(); // Clear previous results
        if (results.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No results found for your search.");
        } else {
            int row = 0, column = 0;
            for (SearchResult result : results) {
                Button resultButton = new Button(result.getResult().toString());
                resultButton.setOnAction(event -> {
                    showAlert(Alert.AlertType.INFORMATION, "Result", "Detail for: " + result.getResult());
                });
                resultsGrid.add(resultButton, column++, row);
                if (column > 2) {
                    column = 0;
                    row++;
                }
            }
        }
    }

    public void onHomeBtnClick() throws IOException {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onCollectionsBtnClick() throws IOException {
        Stage stage = (Stage) collectionBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collections.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onSettingsBtnClick() throws IOException {
        Stage stage = (Stage) settingsBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account-management.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onUploadBtnClick() throws IOException {
        Stage stage = (Stage) uploadBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("upload-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onProfileBtnClick() throws IOException {
        Stage stage = (Stage) profileBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("edit-profile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setSearch(List<SearchResult> results, String query) {
        searchQuery.setText(query);
        resultsGrid.getChildren().clear();
        if (results.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Results", "No results found for " + query + " .");
        } else {
            int row = 0, column = 0;
            for (SearchResult result : results) {
                Button resultButton = new Button(result.getResult().toString());
                resultButton.setOnAction(event -> {
                    showAlert(Alert.AlertType.INFORMATION, "Result", "Detail for: " + result.getResult());
                });
                resultsGrid.add(resultButton, column++, row);
                if (column > 2) {
                    column = 0;
                    row++;
                }
            }
        }
    }
}
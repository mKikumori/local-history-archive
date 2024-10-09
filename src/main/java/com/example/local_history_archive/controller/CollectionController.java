package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionController implements Initializable {

    private CollectionDAO collectionDAO;

    // The active collection object, used to store the currently selected collection
    private Collection activeCollection;

    @FXML
    public Button homeBtn;
    @FXML
    public Button searchBtn;
    @FXML
    public Button updateCollectionBtn;
    @FXML
    public ToggleButton publicBtn;
    @FXML
    public Button collectionBtn;
    @FXML
    private TextField collectionNameField;
    @FXML
    private TextField categoryField;
    @FXML
    public Button settingsBtn;
    @FXML
    public Button registerBtn;
    @FXML
    private Button loginBtn;
    @FXML
    public ComboBox categoryComboBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button uploadBtn;

    private SearchDAO searchDAO;


    public CollectionController() {
        // Initialize the CollectionDAO
        this.collectionDAO = new CollectionDAO();
    }

    // Method to set the active collection (called when a user selects a collection)
    public void setActiveCollection(Collection collection) {
        this.activeCollection = collection;
        if (activeCollection != null) {
            collectionNameField.setText(activeCollection.getCollection_name());
            // Optionally, load the category into the ComboBox if the collection already has a category
            categoryComboBox.setValue(activeCollection.getCategory());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the ComboBox with the category list
        List<String> categories = Arrays.asList("Historical Events", "Natural disasters", "Cultural Heritage", "Local cuisine and recipes", "Architecture and Landmarks", "Old maps and plans",
                "People and Personalities", "Government and Politics", "Photographs and Media", "War contributions and impacts", "Arts and Entertainment", "Natural resource usage");
        categoryComboBox.getItems().addAll(categories);
        if (searchDAO == null) {
            searchDAO = new SearchDAO();
        }
    }

    @FXML
    private void createCollection() throws IOException {
        String collectionName = collectionNameField.getText().trim();

        if (collectionName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Collection name cannot be empty.");
            return;
        }

        // Create a new Collection object
        Collection collection = new Collection(0, collectionName); // 0 is a placeholder for the auto-incremented collection_id
        collectionDAO.newCollection(collection);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection created successfully.");
    }

    @FXML
    private void updateCollection() throws IOException {
        if (activeCollection == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No active collection selected.");
            return;
        }

        String collectionName = collectionNameField.getText().trim();
        if (collectionName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Collection name cannot be empty.");
            return;
        }

        // Pass the collection ID and updated name to the DAO
        collectionDAO.updateCollection(new Collection(activeCollection.getCollection_id(), collectionName));
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection updated successfully.");
    }

    @FXML
    private void deleteCollection() throws IOException {
        if (activeCollection == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No active collection selected.");
            return;
        }

        // Use the active collection's ID to delete the collection
        collectionDAO.deleteCollection(activeCollection.getCollection_id());
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection deleted successfully.");
    }

    @FXML
    private void makeCollectionPublic() throws IOException {
        if (activeCollection == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No active collection selected.");
            return;
        }

        // Use the active collection's ID to make it public
        collectionDAO.makeCollectionPublic(activeCollection.getCollection_id());
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection made public.");
    }

    @FXML
    private void shareCollectionWithUsers(List<Integer> userIds) throws IOException {
        if (userIds.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No users to share the collection with.");
            return;
        }

        if (activeCollection == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No active collection selected.");
            return;
        }

        // Share the active collection with the provided user IDs
        collectionDAO.shareCollectionWithUsers(activeCollection.getCollection_id(), userIds);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection shared with users successfully.");
    }
    @FXML
    private void assignCategoryToCollection() throws IOException {
        if (activeCollection == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No active collection selected.");
            return;
        }

        String selectedCategory = (String) categoryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCategory == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select a category.");
            return;
        }

        // Assign the selected category to the active collection
        collectionDAO.assignCategoryToCollection(activeCollection.getCollection_id(), selectedCategory);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Category assigned successfully.");
    }

    // Navigation methods for UI buttons
    public void onCollectionsBtnClick() throws IOException {
        Stage stage = (Stage) collectionBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collections.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onHomeBtnClick() throws IOException {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onRegisterBtnClick() throws IOException {
        Stage stage = (Stage) registerBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signup-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onLoginBtnClick() throws IOException {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signin-view.fxml"));
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

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void onSearchClicked() throws IOException {
        UserAccount currentUser = SessionManager.getCurrentUser();  // Retrieve the current logged-in user

        if (currentUser != null) {
            String query = searchField.getText().trim();

            if (query.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Query", "Please enter a search query.");
                return;
            }

            List<SearchResult> results = searchDAO.searchAcrossTables(query);

            Stage stage = (Stage) searchBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("searchpage-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);

            // Pass the search object to the next controller
            SearchController controller = fxmlLoader.getController();
            controller.setSearch(results, query);

            stage.setScene(scene);

        } else {
            showAlert(Alert.AlertType.ERROR, "Search Error", "Please log in first.");
        }
    }
}
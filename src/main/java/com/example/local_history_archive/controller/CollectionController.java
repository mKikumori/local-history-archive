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
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class CollectionController implements Initializable {

    private CollectionDAO collectionDAO;
    private SearchDAO searchDAO;

    @FXML
    public GridPane collectionsGrid;
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
    public ComboBox<String> categoryComboBox;
    @FXML
    private TextField searchField;
    @FXML
    private Button uploadBtn;


    public CollectionController() {
        // Initialize the CollectionDAO and SearchDAO
        this.collectionDAO = new CollectionDAO();
        this.searchDAO = new SearchDAO();
        this.categoryComboBox = new ComboBox<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate the ComboBox with the category list
        List<String> categories = Arrays.asList(
                "Historical Events", "Natural disasters", "Cultural Heritage",
                "Local cuisine and recipes", "Architecture and Landmarks",
                "Old maps and plans", "People and Personalities",
                "Government and Politics", "Photographs and Media",
                "War contributions and impacts", "Arts and Entertainment",
                "Natural resource usage"
        );
        categoryComboBox.getItems().addAll(categories);
        loadCollectionsFromDatabase();
    }

    private void loadCollectionsFromDatabase() {

        UserAccount currentUser = SessionManager.getCurrentUser();

        int column = 0;
        int row = 0;



    }

    public void createCollectionBtn() throws IOException {

        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {

            String collectionName = collectionNameField.getText();
            int userId = currentUser.getUserId();

            if (collectionName.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill in the required field.");
                return;
            }

            if (collectionDAO.userHasCollection(userId)) {
                showAlert(Alert.AlertType.ERROR, "Creation Error", "User already have a collection.");
                return;
            }

            Collection collection = new Collection(userId, collectionName, null, null);

            collectionDAO = new CollectionDAO();
            collectionDAO.newCollection(collection);
            showAlert(Alert.AlertType.INFORMATION, "Collection Creation Successful", "Your collection has been created.");

            Stage stage = (Stage) collectionBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collections.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);
        }
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

            List<SearchResult> results = searchDAO.searchUploadsByTitle(query);

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
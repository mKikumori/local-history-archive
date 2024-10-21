package com.example.local_history_archive.controller;

import com.example.local_history_archive.Base64ToImage;
import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The controller class for the collections pages
 */
public class CollectionController implements Initializable {


    private CollectionDAO collectionDAO;
    private SearchDAO searchDAO;
    private Collection collection;
    private UserAccountDAO userAccountDAO;

    @FXML
    public GridPane collectionsGrid;
    @FXML
    public Button userCollection;
    @FXML
    public ImageView userImage;
    @FXML
    public Label collectiionName;
    @FXML
    public Label userName;
    @FXML
    public Button homeBtn;
    @FXML
    public Button searchBtn;
    @FXML
    public Button collectionBtn;
    @FXML
    private TextField collectionNameField;
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

    UserAccount currentUser = SessionManager.getCurrentUser();

    public CollectionController() {
        this.collectionDAO = new CollectionDAO();
        this.searchDAO = new SearchDAO();
        this.userAccountDAO = new UserAccountDAO();
        this.categoryComboBox = new ComboBox<>();
        this.collectiionName = new Label();
        this.userName = new Label();
        this.collectionsGrid = new GridPane();
        this.userImage = new ImageView();
        this.collectionBtn = new Button();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> categories = Arrays.asList(
                "Historical Events", "Natural disasters", "Cultural Heritage",
                "Local cuisine and recipes", "Architecture and Landmarks",
                "Old maps and plans", "People and Personalities",
                "Government and Politics", "Photographs and Media",
                "War contributions and impacts", "Arts and Entertainment",
                "Natural resource usage"
        );

        collectiionName.setText(collectionDAO.getCollectionNameByUserId(currentUser.getUserId()));
        String username = userAccountDAO.getById(currentUser.getUserId()).getUsername();
        userName.setText("by: " + username);
        categoryComboBox.getItems().addAll(categories);

        loadCollectionsFromDatabase();

        Image placeholderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/image-placeholder.png")));
        Image image = Base64ToImage.base64ToImage(currentUser.getProfilePic());

        if (image == null) {
            userImage.setFitHeight(100);
            userImage.setImage(placeholderImage);
        } else {
            userImage.setFitHeight(100);
            userImage.setImage(image);
        }
    }

    private void loadCollectionsFromDatabase() {

        UserAccount currentUser = SessionManager.getCurrentUser();

        int column = 0;
        int row = 0;

        List<UserUpload> uploads = collectionDAO.getUploadsForUser(currentUser.getUserId());

        collectionsGrid.setHgap(10);
        collectionsGrid.setVgap(10);
        collectionsGrid.setPadding(new Insets(20, 20, 20, 20));

        for (UserUpload upload: uploads) {

            Button uploadBtn = new Button(upload.getUploadName());
            uploadBtn.setPrefWidth(302);
            uploadBtn.setPrefHeight(107);
            uploadBtn.setMaxWidth(302);
            uploadBtn.setMaxHeight(107);

            uploadBtn.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 14px; -fx-text-fill: black;");

            VBox contentBox = new VBox();
            contentBox.setAlignment(Pos.CENTER);
            contentBox.setSpacing(5);

            ImageView imageView = new ImageView();

            if (upload.getUploadType().equals("image") && upload.getImageData() != null) {
                Image image = Base64ToImage.base64ToImage(upload.getImageData());

                imageView.setImage(image);
                imageView.setFitHeight(70);
                imageView.setFitWidth(120);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);

                contentBox.getChildren().add(imageView);
            }

            Label uploadLabel = new Label(upload.getUploadName());
            uploadLabel.setWrapText(true);
            uploadLabel.setMaxWidth(120);
            uploadLabel.setPrefHeight(30);

            uploadBtn.setGraphic(contentBox);

            uploadBtn.setOnAction(actionEvent -> {
                try {
                    openUploadDetails(upload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            collectionsGrid.add(uploadBtn, column, row);

            column++;
            if (column > 3) {
                column = 0;
                row++;
            }
        }
    }

    private void openUploadDetails(UserUpload upload) throws IOException {
        Stage stage = (Stage) collectionsGrid.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("upload-details.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);

        // Pass the upload object to the next controller
        UploadDetailsController controller = fxmlLoader.getController();
        controller.setUpload(upload);

        stage.setScene(scene);
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

    public void userCollectionBtn() throws IOException {
        Stage stage = (Stage) userCollection.getScene().getWindow();
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
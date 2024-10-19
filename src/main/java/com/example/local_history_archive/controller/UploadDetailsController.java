package com.example.local_history_archive.controller;

import com.example.local_history_archive.Base64ToImage;
import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;

import java.util.Collections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class UploadDetailsController {
    // The active collection object, used to store the currently selected collection
    private Collection activeCollection;
    @FXML
    public Button homeBtn;
    @FXML
    public Button collectionBtn;
    @FXML
    public Button settingsBtn;
    @FXML
    public Button profileBtn;
    @FXML
    public Label uploadDate;
    @FXML
    public Label uploadCategory;
    @FXML
    public GridPane uploadGrid;
    @FXML
    public TextField searchField;
    @FXML
    public Button searchBtn;
    @FXML
    private Label uploadNameLabel;
    @FXML
    private Label uploadDescriptionLabel;
    @FXML
    public ImageView uploadImage;
    @FXML
    private Button uploadBtn;

    private UserUpload upload;

    private UserUploadDAO userUploadDAO;

    private SearchDAO searchDAO;

    private CollectionDAO collectionDAO;

    public void initialize() {
        if (userUploadDAO == null) {
            userUploadDAO = new UserUploadDAO();
        }
        loadUploadsFromDatabase();
        if (searchDAO == null) {
            searchDAO = new SearchDAO();
        }
        if (collectionDAO == null) {
            collectionDAO = new CollectionDAO();
        }
    }

    public UploadDetailsController() {
        this.userUploadDAO = new UserUploadDAO();
    }

    public void deleteUpload() {
        int uploadId = upload.getUploadId();
        int userId = SessionManager.getCurrentUser().getUserId();

        boolean isDeleted = collectionDAO.deleteUploadByCreator(uploadId, userId);

        if (isDeleted) {
            showAlert(Alert.AlertType.INFORMATION, "Upload Deletion", "Upload deleted successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Deletion", "Failed to delete upload. You may not be the creator.");
        }
    }

    public void deleteCollectionUpload() {
        int uploadId = upload.getUploadId();
        int userId = SessionManager.getCurrentUser().getUserId();

        boolean isRemoved = collectionDAO.removeUploadFromCollection(uploadId, userId);

        if (isRemoved) {
            showAlert(Alert.AlertType.INFORMATION, "Upload Deletion from Collection", "Upload removed from collection successfully.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Deletion from Collection", "Failed to remove upload from collection.\nCheck if this upload is save into a collection in the first place.");
        }
    }

    private void loadUploadsFromDatabase() {
        if (userUploadDAO == null) {
            System.err.println("UserUploadDAO is not initialized.");
            return;
        }

        List<UserUpload> uploads = userUploadDAO.allUploads();
        Collections.shuffle(uploads);

        // Set grid properties: gaps and alignment
        uploadGrid.setHgap(10); // Horizontal gap between columns
        uploadGrid.setVgap(10); // Vertical gap between rows
        uploadGrid.setPadding(new Insets(20, 20, 20, 20)); // Padding around the grid

        int column = 0;
        int row = 0;
        int uploadCount = 0;

        for (UserUpload upload: uploads) {

            if (uploadCount >= 3) {
                break;
            }

            Button uploadBtn = new Button(upload.getUploadName());

            uploadBtn.setPrefWidth(302); // Set button width
            uploadBtn.setPrefHeight(107); // Set button height
            uploadBtn.setMaxWidth(302);   // Limit max button width
            uploadBtn.setMaxHeight(107);  // Limit max button height

            // Apply uniform style
            uploadBtn.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-background-radius: 5; -fx-font-size: 14px; -fx-text-fill: black;");

            // Create a VBox to stack the image and the text
            VBox contentBox = new VBox();
            contentBox.setAlignment(Pos.CENTER); // Center alignment for the content
            contentBox.setSpacing(5); // Space between image and text

            ImageView imageView = new ImageView();

            if (upload.getUploadType().equals("image") && upload.getImageData() != null) {

                Image image = Base64ToImage.base64ToImage(upload.getImageData());

                imageView.setImage(image);
                imageView.setFitHeight(70); // Adjust uniform image height
                imageView.setFitWidth(120);  // Adjust uniform image width
                imageView.setPreserveRatio(true); // Preserve the aspect ratio
                imageView.setSmooth(true); // Improve image quality

                contentBox.getChildren().add(imageView); // Add image to VBox
            }

            // Add text regardless of the image presence
            Label uploadLabel = new Label(upload.getUploadName());
            uploadLabel.setWrapText(true); // Wrap text to fit inside the button
            uploadLabel.setMaxWidth(120);  // Limit the width of the text
            uploadLabel.setPrefHeight(30); // Set a fixed height for the label

            // Set the VBox as the button's graphic
            uploadBtn.setGraphic(contentBox);

            uploadBtn.setOnAction(actionEvent -> {
                try {
                    openUploadDetails(upload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            uploadGrid.add(uploadBtn, column, row);

            column++;
            uploadCount++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
    private void openUploadDetails(UserUpload upload) throws IOException {
        Stage stage = (Stage) uploadGrid.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("search-clicked.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);

        // Pass the upload object to the next controller
        UploadDetailsController controller = fxmlLoader.getController();
        controller.setUpload(upload);

        stage.setScene(scene);
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

    public void onUploadBtnClick() throws IOException {
        Stage stage = (Stage) uploadBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("upload-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onSettingsBtnClick() throws IOException {
        Stage stage = (Stage) settingsBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account-management.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onProfileBtnClick() throws IOException {
        Stage stage = (Stage) profileBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("edit-profile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void setUpload(UserUpload upload) {
        this.upload = upload;

        uploadNameLabel.setText(upload.getUploadName());
        uploadDescriptionLabel.setText(upload.getUploadDescription());
        uploadDate.setText(upload.getUploadedAt());
        uploadCategory.setText(upload.getUploadCategories());

        if (upload.getUploadType().equals("image") && upload.getImageData() != null) {
            uploadImage.setImage(Base64ToImage.base64ToImage(upload.getImageData()));
        } else {
            uploadImage.setImage(null);
        }
    }

    public void setUpload(SearchResult result) {
        UserUpload upload = userUploadDAO.getUploadById(result.getId());

        if (upload != null) {
            uploadNameLabel.setText(upload.getUploadName());
            uploadDescriptionLabel.setText(upload.getUploadDescription());
            uploadDate.setText(upload.getUploadedAt());
            uploadCategory.setText(upload.getUploadCategories());


            if (upload.getUploadType().equals("image") && upload.getImageData() != null) {
                uploadImage.setImage(Base64ToImage.base64ToImage(upload.getImageData()));
            } else {
                uploadImage.setImage(null);
            }
        }
    }

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

    public void saveToCollection() {
        UserAccount currentUser = SessionManager.getCurrentUser();
        String collectionName = String.valueOf(uploadNameLabel);
        int userId = currentUser.getUserId();
        int uploadId = upload.getUploadId();

        if (collectionName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Collection name cannot be empty.");
            return;
        }

        collectionDAO.addUploadToCollection(userId, uploadId);

        showAlert(Alert.AlertType.INFORMATION, "Success", "Upload saved successfully.");
    }
}


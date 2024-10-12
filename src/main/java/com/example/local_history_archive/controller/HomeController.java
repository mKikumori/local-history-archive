package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import com.example.local_history_archive.Base64ToImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

public class HomeController {
    @FXML
    public Button collectionBtn;
    @FXML
    public Button settingsBtn;
    @FXML
    public Button profileBtn;
    @FXML
    public GridPane uploadGrid;
    @FXML
    public Button searchBtn;
    @FXML
    public TextField searchField;
    @FXML
    private Button uploadBtn;

    private UserUploadDAO userUploadDAO;

    private SearchDAO searchDAO;

    public void initialize() {
        if (userUploadDAO == null) {
            userUploadDAO = new UserUploadDAO();
        }
        if (searchDAO == null) {
            searchDAO = new SearchDAO();
        }
        loadUploadsFromDatabase();
    }

    private void loadUploadsFromDatabase() {
        if (userUploadDAO == null) {
            System.err.println("UserUploadDAO is not initialized.");
            return;
        }

        List<UserUpload> uploads = userUploadDAO.allUploads();

        // Set grid properties: gaps and alignment
        uploadGrid.setHgap(10); // Horizontal gap between columns
        uploadGrid.setVgap(10); // Vertical gap between rows
        uploadGrid.setPadding(new Insets(20, 20, 20, 20)); // Padding around the grid

        int column = 0;
        int row = 0;

        for (UserUpload upload : uploads) {
            // Create a button for each upload
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

            // If the upload is an image, display it
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

            // Set an action on button click
            uploadBtn.setOnAction(actionEvent -> {
                try {
                    openUploadDetails(upload); // Function to open upload details
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Add button to the grid
            uploadGrid.add(uploadBtn, column, row);

            // Update column and row for the grid
            column++;
            if (column > 3) { // 3 items per row, adjust to your needs
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

    public void onUploadBtnClick() throws IOException {
        Stage stage = (Stage) uploadBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("upload-view.fxml"));
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

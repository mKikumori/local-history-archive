package com.example.local_history_archive.controller;

import com.example.local_history_archive.Base64ToImage;
import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.SessionManager;
import com.example.local_history_archive.model.UserAccount;
import com.example.local_history_archive.model.UserUpload;
import com.example.local_history_archive.model.UserUploadDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;
import javafx.scene.image.Image;

public class HelloController {

    @FXML
    private Button registerBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private GridPane uploadsGrid;

    private UserUploadDAO userUploadDAO;

    public void initialize() {
        if (userUploadDAO == null) {
            userUploadDAO = new UserUploadDAO();
        }
        loadUploadsFromDatabase();
    }

    UserAccount currentUser = SessionManager.getCurrentUser();

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadUploadsFromDatabase() {
        if (userUploadDAO == null) {
            System.err.println("UserUploadDAO is not initialized.");
            return;
        }

        List<UserUpload> uploads = userUploadDAO.allUploads();

        // Set grid properties: gaps and alignment
        uploadsGrid.setHgap(10); // Horizontal gap between columns
        uploadsGrid.setVgap(10); // Vertical gap between rows
        uploadsGrid.setPadding(new Insets(20, 20, 20, 20)); // Padding around the grid

        int column = 0;
        int row = 0;

        for (UserUpload upload: uploads) {
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

            uploadsGrid.add(uploadBtn, column, row);

            column++;
            if (column > 3) {
                column = 0;
                row++;
            }
        }
    }

    private void openUploadDetails(UserUpload upload) throws IOException {
        if (currentUser != null) {
            Stage stage = (Stage) uploadsGrid.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("search-clicked.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);

            // Pass the upload object to the next controller
            UploadDetailsController controller = fxmlLoader.getController();
            controller.setUpload(upload);

            stage.setScene(scene);
        } else {
            showAlert(Alert.AlertType.ERROR, "User Error", "Please log in.");
        }

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

    public void onUploadBtnClick() throws IOException {
        showAlert(Alert.AlertType.ERROR, "User Error", "Please log in.");
    }

    public void onCollectionsBtnClick() throws IOException {
        showAlert(Alert.AlertType.ERROR, "User Error", "Please log in.");
    }

    public void onSettingsBtnClick() throws IOException {
        showAlert(Alert.AlertType.ERROR, "User Error", "Please log in.");
    }
}
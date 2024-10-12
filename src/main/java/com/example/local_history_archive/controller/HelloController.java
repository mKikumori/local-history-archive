package com.example.local_history_archive.controller;

import com.example.local_history_archive.Base64ToImage;
import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.SessionManager;
import com.example.local_history_archive.model.UserAccount;
import com.example.local_history_archive.model.UserUpload;
import com.example.local_history_archive.model.UserUploadDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
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

        int column = 0;
        int row = 0;

        for (UserUpload upload: uploads) {
            Button uploadBtn = new Button(upload.getUploadName());
            ImageView imageView = new ImageView();

            if (upload.getUploadType().equals("image") && upload.getImageData() != null) {

                Image image = Base64ToImage.base64ToImage(upload.getImageData());

                imageView.setImage(image);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);

                uploadBtn.setGraphic(imageView);
            } else {
                uploadBtn.setText(upload.getUploadName());
            }

            uploadBtn.setOnAction(actionEvent -> {
                try {
                    openUploadDetails(upload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            uploadsGrid.add(uploadBtn, column, row);

            column++;
            if (column == 3) {
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signup-view.fxml"));
            Stage signUpStage = new Stage();
            signUpStage.initModality(Modality.APPLICATION_MODAL);
            signUpStage.setTitle("Sign Up");
            Scene scene = new Scene(loader.load());
            signUpStage.setScene(scene);
            signUpStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onLoginBtnClick() throws IOException {
        try {
            // Load the sign-in FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("signin-view.fxml"));
            Stage signInStage = new Stage();
            signInStage.initModality(Modality.APPLICATION_MODAL);
            signInStage.setTitle("Sign In");

            // Create and set the scene
            Scene scene = new Scene(loader.load());
            signInStage.setScene(scene);

            // Show the sign-in pop-up
            signInStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
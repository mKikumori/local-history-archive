package com.example.local_history_archive.controller;

import com.example.local_history_archive.Base64ToImage;
import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.ImageToBase64;
import com.example.local_history_archive.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Objects;

public class EditProfileController {
    @FXML
    public Button homeBtn;
    @FXML
    public Button collectionBtn;
    @FXML
    public Button settingsBtn;
    @FXML
    public Button uploadBtn;
    @FXML
    public ImageView uploadImage;
    @FXML
    public Button changeBtn;
    @FXML
    public TextField bioTextField;
    @FXML
    public TextField usernameTextField;
    @FXML
    public Button accManagementBtn;
    @FXML
    private String encodedFile;

    public String fileType;

    private UserUpload upload;

    private UserAccountDAO userAccountDAO;

    public void initialize() {
        if (userAccountDAO == null) {
            userAccountDAO = new UserAccountDAO();
        }
        Image placeholderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/image-placeholder.png")));
        uploadImage.setImage(placeholderImage);
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

    public void onManagementClick() throws IOException{
        Stage stage = (Stage) settingsBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account-management.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onChangeBtnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg")
        );

        Stage stage = (Stage) changeBtn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String filePath = file.getAbsolutePath();
            System.out.println("Selected file: " + filePath);

            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                Image image = new Image(file.toURI().toString());
                uploadImage.setImage(image);
                encodedFile = ImageToBase64.encodeImageToBase64(filePath);
                fileType = "image";
                System.out.println("Image successfully encoded to base64.");
            } else {
                showAlert(Alert.AlertType.WARNING, "File Type Not Supported", "Only images can be uploaded for now.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No File Selected", "Please select a file to upload.");
        }
    }

    @FXML
    private void onPublishBtnClick() {

        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            String username = usernameTextField.getText();
            int userId = currentUser.getUserId();
            String user_email = currentUser.getUserEmail();
            String user_password = currentUser.getPassword();
            String bio = bioTextField.getText();
            String fileBase64 = encodedFile;

            UserAccount userAccount = new UserAccount(userId, user_email, username, user_password, bio, fileBase64);
            userAccountDAO.updateUser(userAccount);

            showAlert(Alert.AlertType.INFORMATION, "Profile Edited Successfully", "Your profile has been updated.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Error", "Please log in.");
        }
    }

    @FXML
    private void onDiscardBtnClick() {
        usernameTextField.clear();
        bioTextField.clear();
        showAlert(Alert.AlertType.INFORMATION, "Fields Cleared", "The form has been reset.");
    }

    public void setUpload(UserUpload upload) {
        this.upload = upload;

        if (upload.getUploadType().equals("image") && upload.getImageData() != null) {
            uploadImage.setImage(Base64ToImage.base64ToImage(upload.getImageData()));
        } else {
            showAlert(Alert.AlertType.ERROR, "Error!", "Please enter an image.");
        }
    }
}

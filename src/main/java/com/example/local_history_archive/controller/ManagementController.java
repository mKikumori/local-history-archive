package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.SessionManager;
import com.example.local_history_archive.model.UserAccount;
import com.example.local_history_archive.model.UserAccountDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class ManagementController {
    @FXML
    public Button collectionBtn;
    @FXML
    public Button uploadBtn;
    @FXML
    public Button homeBtn;
    @FXML
    public TextField emailTextField;
    @FXML
    public PasswordField passwordTextField;
    @FXML
    public Button deleteBtn;
    @FXML
    public Button saveBtn;
    @FXML
    public Button editProfileBtn;
    @FXML
    public Button changeBtn;

    private UserAccountDAO userAccountDAO;

    public void initialize() {
        if (userAccountDAO == null) {
            userAccountDAO = new UserAccountDAO();
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

    public void onUploadBtnClick() throws IOException {
        Stage stage = (Stage) uploadBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("upload-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onChangeBtnClick() throws IOException {
        Stage stage = (Stage) uploadBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("resetpassword-view.fxml"));
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
    private void onDiscardBtnClick() {
        emailTextField.clear();
        passwordTextField.clear();
        showAlert(Alert.AlertType.INFORMATION, "Fields Cleared", "The form has been reset.");
    }

    @FXML
    private void onSaveBtnClick() {

        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            int userId = currentUser.getUserId();
            String user_email = emailTextField.getText();
            String user_password = passwordTextField.getText();
            String username = currentUser.getUsername();
            String bio = currentUser.getBio();
            String profile_picture = currentUser.getProfilePic();

            if (user_email.isEmpty() || user_password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all required fields.");
                return;
            }

            if (currentUser.getPassword().equals(user_password)) {
                UserAccount userAccount = new UserAccount(userId, user_email, username, currentUser.getPassword(), bio, profile_picture);
                userAccountDAO.updateUser(userAccount);
                showAlert(Alert.AlertType.INFORMATION, "Email Edited Successfully", "Your account information has been updated.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Error", "Incorrect password. Please try again.");
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Error", "Please log in.");
        }
    }

    public void onDeleteBtnClick() throws IOException {

        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {

            int userId = currentUser.getUserId();
            userAccountDAO.deleteAccount(userId);

            SessionManager.clearSession();

            Stage stage = (Stage) uploadBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

            showAlert(Alert.AlertType.INFORMATION, "Account Deletion Successfully", "Your account has been deleted.");

        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Error", "Please log in.");
        }
    }

    public void onEditProfileClick() throws IOException{
        Stage stage = (Stage) editProfileBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("edit-profile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}

package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

/**
 * The controller class for the reset password page
 */
public class ResetPasswordController {
    @FXML
    public Button submitBtn;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField confirmTextField;

    private UserAccountDAO userAccountDAO;

    private UserUploadDAO userUploadDAO;

    @FXML
    public void initialize() {
        if (userUploadDAO == null) {
            userUploadDAO = new UserUploadDAO();
        }
        userAccountDAO = new UserAccountDAO();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void onResetPasswordBtnClick() throws IOException {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();
        String confirmPassword = confirmTextField.getText();
        UserAccount currentUser = SessionManager.getCurrentUser();
        int userId = currentUser.getUserId();
        String bio = currentUser.getBio();
        String username = currentUser.getUsername();
        String profile_pic = currentUser.getProfilePic();

        if (confirmPassword.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all required fields.");
            return;
        }

        UserAccount userAccount = userAccountDAO.getByEmail(email);

        if (userAccount == null) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "No user found with the given email.");
            return;
        }

        if (userAccount.getPassword().equals(password)) {

            UserAccount updatedUser = new UserAccount(userId, email, username, confirmPassword, bio, profile_pic);

            userAccountDAO.updateUser(updatedUser);

            showAlert(Alert.AlertType.INFORMATION, "Password Edited Successfully", "Your password has been updated.");

            Stage stage = (Stage) submitBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Error", "Password update failed.");
        }
    }
}

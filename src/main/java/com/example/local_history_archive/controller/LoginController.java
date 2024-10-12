package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.Modality;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class LoginController {

    @FXML
    public Hyperlink registerLink;
    @FXML
    public Button onRegisterBtnClick;
    @FXML
    private Button loginBtn;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private UserAccountDAO userAccountDAO;

    private UserUploadDAO userUploadDAO;


    @FXML
    private void goToResetPasswordPage() {
        try {
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("resetpassword-view.fxml"));
            Stage resetPasswordStage = new Stage();
            resetPasswordStage.initModality(Modality.APPLICATION_MODAL);
            resetPasswordStage.setTitle("Reset Password");
            Scene scene = new Scene(loader.load());
            resetPasswordStage.setScene(scene);
            resetPasswordStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    public void onRegisterBtnClick() throws IOException {
        try {
            // Load the sign-up FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signup-view.fxml"));
            Stage signUpStage = new Stage();
            signUpStage.initModality(Modality.APPLICATION_MODAL);  // Set modality to block other windows
            signUpStage.setTitle("Sign Up");

            // Create and set the scene with specified dimensions
            Scene scene = new Scene(fxmlLoader.load());
            signUpStage.setScene(scene);

            // Show the sign-up modal window
            signUpStage.showAndWait();  // Use showAndWait to block interaction with other windows
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerLink() throws IOException {
        Stage stage = (Stage) registerLink.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("resetpassword-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void loginSubmitBtn() throws IOException {
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all required fields.");
            return;
        }

        UserAccount userAccount = userAccountDAO.getByEmail(email);

        if (userAccount == null) {
            showAlert(Alert.AlertType.ERROR, "Login Error", "No user found with the given email.");
            return;
        }

        if (userAccount.getPassword().equals(password)) {

            SessionManager.setCurrentUser(userAccount);

            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome back, " + userAccount.getUsername() + "!");

            Stage stage = (Stage) loginBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Incorrect password. Please try again.");
        }
    }
}

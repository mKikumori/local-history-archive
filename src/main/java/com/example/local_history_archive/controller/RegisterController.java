package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
 * The controller class for the sign-up page
 */
public class RegisterController {

    @FXML
    public Button registerBtn;
    @FXML
    public Hyperlink loginLink;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private UserAccountDAO userAccountDAO;

    private UserUploadDAO userUploadDAO;

    @FXML
    public void initialize() {
        if (userUploadDAO == null) {
            userUploadDAO = new UserUploadDAO();
        }
        userAccountDAO = new UserAccountDAO();
    }

    public void onLoginClick() throws IOException {
        Stage stage = (Stage) loginLink.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signin-view.fxml"));
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
    private void registerSubmitBtn() {
        String username = usernameTextField.getText();
        String email = emailTextField.getText();
        String password = passwordTextField.getText();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all required fields.");
            return;
        }

        UserAccount newUser = new UserAccount(email, username, password, "", "");

        try {
            userAccountDAO.newUser(newUser);

            UserAccount userAccount = userAccountDAO.getByEmail(email);
            SessionManager.setCurrentUser(userAccount);
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful!", "User " + userAccount.getUsername() + " registered successfully.");

            usernameTextField.clear();
            emailTextField.clear();
            passwordTextField.clear();

            Stage stage = (Stage) registerBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while registering the user.");
            e.printStackTrace();
        }
    }
}

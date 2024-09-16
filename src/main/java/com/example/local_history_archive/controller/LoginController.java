package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.model.UserAccount;
import com.example.local_history_archive.model.UserAccountDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button registerBtn;
    @FXML
    private Button loginBtn;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private UserAccountDAO userAccountDAO;

    public LoginController() {
        userAccountDAO = new UserAccountDAO();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void registerSubmitBtn() throws IOException {
        Stage stage = (Stage) registerBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("signup-view.fxml"));
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

            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + userAccount.getUsername() + "!");

            Stage stage = (Stage) loginBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
            stage.setScene(scene);

        } else {
            showAlert(Alert.AlertType.ERROR, "Login Error", "Incorrect password. Please try again.");
        }
    }
}

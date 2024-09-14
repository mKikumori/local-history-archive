package com.example.local_history_archive.controller;

import com.example.local_history_archive.model.UserAccount;
import com.example.local_history_archive.model.UserAccountDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RegisterController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField emailTextField;
    @FXML
    private PasswordField passwordTextField;

    private UserAccountDAO userAccountDAO;

    public RegisterController() {
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
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful!", "User registered successfully.");

            usernameTextField.clear();
            emailTextField.clear();
            passwordTextField.clear();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while registering the user.");
            e.printStackTrace();
        }
    }
}

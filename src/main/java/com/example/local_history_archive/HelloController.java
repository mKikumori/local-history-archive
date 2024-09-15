package com.example.local_history_archive;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HelloController {

    @FXML
    private Pane overlayPane; // This is for showing overlay if needed

    @FXML
    private Button signInButton;

    @FXML
    private void initialize() {
        // Initialization logic, if any
    }

    @FXML
    private void goToSignInPage() {
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
    @FXML
    private void goToSignUpPage() {
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

    @FXML
    private void goToResetPasswordPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("resetpassword-view.fxml"));
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

}


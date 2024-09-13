package com.example.local_history_archive;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloController {

    @FXML
    private void goToSignInPage() {
        try {
            // Load the Sign In page FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent signInRoot = fxmlLoader.load();

            // Get the current stage
            Stage stage = (Stage) signInRoot.getScene().getWindow();

            // Set the scene to the sign-in page
            Scene signInScene = new Scene(signInRoot);
            stage.setScene(signInScene);
            stage.setTitle("Sign In Page");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error appropriately
        }
    }
}

package com.example.local_history_archive;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class signin {

    private Stage parentStage;

    @FXML
    private Hyperlink forgotPasswordLink;

    @FXML
    private void initialize() {
        forgotPasswordLink.setOnAction(e -> openResetPasswordPage());
    }

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    private void openResetPasswordPage() {
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

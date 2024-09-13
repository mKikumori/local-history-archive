package com.example.local_history_archive;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Test
public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Local History Archive");
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void goToSignInPage(java.fx.event.ActionEvent event){
            FXMLLoader fxmlLoader = new FXMLLoader(get.class().getResource("login.fxml"));
            parent loginroot = fxmlLoader.load();

            Stage stage = (Stage)(javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene loginScene = new Scene(loginroot);
            stage.setScene(loginScene);
            stage,setTitle("Login Page");
            stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}

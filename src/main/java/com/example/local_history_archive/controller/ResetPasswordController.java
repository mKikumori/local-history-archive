package com.example.local_history_archive.controller;

import com.example.local_history_archive.model.UserAccountDAO;
import com.example.local_history_archive.model.UserUpload;
import com.example.local_history_archive.model.UserUploadDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;
import java.util.List;

public class ResetPasswordController {
    @FXML
    public GridPane uploadsGrid;

    private UserAccountDAO userAccountDAO;

    private UserUploadDAO userUploadDAO;

    @FXML
    public void initialize() {
        if (userUploadDAO == null) {
            userUploadDAO = new UserUploadDAO();
        }
        userAccountDAO = new UserAccountDAO();
        loadUploadsFromDatabase();
    }

    private void loadUploadsFromDatabase() {
        if (userUploadDAO == null) {
            System.err.println("UserUploadDAO is not initialized.");
            return;
        }

        List<UserUpload> uploads = userUploadDAO.allUploads();

        int column = 0;
        int row = 0;

        for (UserUpload upload: uploads) {
            Button uploadBtn = new Button(upload.getUploadName());
            ImageView imageView = new ImageView();

            if (upload.getUploadType().equals("image") && upload.getImageData() != null) {

                byte[] imageBytes = Base64.getDecoder().decode(upload.getImageData());
                InputStream imageStream = new ByteArrayInputStream(imageBytes);

                Image image = new Image(imageStream);
                imageView.setImage(image);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);

                uploadBtn.setGraphic(imageView);
            } else {
                uploadBtn.setText(upload.getUploadName());
            }

            uploadsGrid.add(uploadBtn, column, row);

            column++;
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
}

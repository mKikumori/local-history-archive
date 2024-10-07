package com.example.local_history_archive.controller;

import com.example.local_history_archive.model.Collection;
import com.example.local_history_archive.model.CollectionDAO;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.List;

public class CollectionController {

    private CollectionDAO collectionDAO;
    @FXML
    public Button updateBtn;


    @FXML
    private TextField collectionNameField;

    public CollectionController() {
        // Initialize the CollectionDAO
        this.collectionDAO = new CollectionDAO();
    }

    @FXML
    private void createCollection() throws IOException {
        String collectionName = collectionNameField.getText().trim();

        if (collectionName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Collection name cannot be empty.");
            return;
        }

        Collection collection = new Collection(0, collectionName); // 0 because collection_id is auto-incremented
        collectionDAO.newCollection(collection);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection created successfully.");
    }

    @FXML
    private void updateCollection(int collectionId) throws IOException {
        String collectionName = collectionNameField.getText().trim();

        if (collectionName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Collection name cannot be empty.");
            return;
        }

        Collection collection = new Collection(collectionId, collectionName);
        collectionDAO.updateCollection(collection);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection updated successfully.");
    }

    @FXML
    private void deleteCollection(int collectionId) throws IOException {
        collectionDAO.deleteCollection(collectionId);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection deleted successfully.");
    }


    @FXML
    private void makeCollectionPublic(int collectionId) throws IOException {
        collectionDAO.makeCollectionPublic(collectionId);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection made public.");
    }


    @FXML
    private void shareCollectionWithUsers(int collectionId, List<Integer> userIds) throws IOException {
        if (userIds.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "No users to share the collection with.");
            return;
        }

        collectionDAO.shareCollectionWithUsers(collectionId, userIds);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Collection shared with users successfully.");
    }


    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

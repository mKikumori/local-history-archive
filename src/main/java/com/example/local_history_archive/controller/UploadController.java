package com.example.local_history_archive.controller;

import com.example.local_history_archive.HelloApplication;
import com.example.local_history_archive.ImageToBase64;
import com.example.local_history_archive.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The controller class for the upload page
 */
public class UploadController implements Initializable {
    @FXML
    public Button homeBtn;
    @FXML
    public Button collectionBtn;
    @FXML
    public Button settingsBtn;
    @FXML
    public Button profileBtn;
    @FXML
    public Button submitBtn;
    @FXML
    public Button discardBtn;
    @FXML
    public Button uploadBtn;
    @FXML
    public TextField uploadTitleTestField;
    @FXML
    public TextArea uploadDescriptionTextField;
    @FXML
    public ComboBox uploadCategory;
    @FXML
    public ImageView uploadImage;
    @FXML
    public Button searchBtn;
    @FXML
    public TextField searchField;
    @FXML
    public Button createCollectionBtn;
    @FXML
    private String encodedFile;

    public String fileType;

    private UserUploadDAO userUploadDAO;

    private SearchDAO searchDAO;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<String> categories = Arrays.asList("Historical Events", "Natural disasters", "Cultural Heritage", "Local cuisine and recipes", "Architecture and Landmarks", "Old maps and plans",
                "People and Personalities", "Government and Politics", "Photographs and Media", "War contributions and impacts", "Arts and Entertainment", "Natural resource usage");
        uploadCategory.getItems().addAll(categories);
        Image placeholderImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/image-placeholder.png")));
        uploadImage.setImage(placeholderImage);
        if (searchDAO == null) {
            searchDAO = new SearchDAO();
        }
    }

    public void onSearchClicked() throws IOException {
        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            String query = searchField.getText().trim();

            if (query.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Missing Query", "Please enter a search query.");
                return;
            }

            List<SearchResult> results = searchDAO.searchUploadsByTitle(query);

            Stage stage = (Stage) searchBtn.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("searchpage-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);

            // Pass the search object to the next controller
            SearchController controller = fxmlLoader.getController();
            controller.setSearch(results, query);

            stage.setScene(scene);

        } else {
            showAlert(Alert.AlertType.ERROR, "Search Error", "Please log in first.");
        }
    }

    public void onHomeBtnClick() throws IOException {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onCollectionsBtnClick() throws IOException {
        Stage stage = (Stage) collectionBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("collections.fxml"));
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
    private void onUploadBtnClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Images", "*.jpg", "*.png", "*.jpeg"),
                new FileChooser.ExtensionFilter("Documents", "*.pdf", "*.docx", "*.txt")
        );

        Stage stage = (Stage) uploadBtn.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            String filePath = file.getAbsolutePath();
            System.out.println("Selected file: " + filePath);

            if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg") || filePath.endsWith(".png")) {
                Image image = new Image(file.toURI().toString());
                uploadImage.setImage(image);
                fileType = "image";
                encodedFile = ImageToBase64.encodeImageToBase64(filePath);
                System.out.println("Image successfully encoded to base64.");
            } else if (filePath.endsWith(".pdf") || filePath.endsWith(".docx") || filePath.endsWith(".txt")) {
                try {
                    fileType = "document";
                    encodedFile = new String(Files.readAllBytes(file.toPath()));
                    System.out.println("Document successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to encode the document.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "File Type Not Supported", "Only images can be uploaded for now.");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "No File Selected", "Please select a file to upload.");
        }
    }

    @FXML
    private void onPublishBtnClick() {

        UserAccount currentUser = SessionManager.getCurrentUser();

        if (currentUser != null) {
            String title = uploadTitleTestField.getText();
            int userId = currentUser.getUserId();
            String description = uploadDescriptionTextField.getText();
            String category = (String) uploadCategory.getValue();

            if (title.isEmpty() || description.isEmpty() || category == null || fileType == null) {
                showAlert(Alert.AlertType.ERROR, "Missing Information", "Please fill in all the fields.");
                return;
            }

            String fileBase64 = encodedFile;

            UserUpload userUpload = new UserUpload(userId, title, category, fileType, description, false, fileBase64, null);

            userUploadDAO = new UserUploadDAO();
            userUploadDAO.newUpload(userUpload);
            showAlert(Alert.AlertType.INFORMATION, "Upload Successful", "Your file has been published.");
        } else {
            showAlert(Alert.AlertType.ERROR, "Upload Error", "Please log in first.");
        }
    }

    @FXML
    private void onDiscardBtnClick() {
        uploadTitleTestField.clear();
        uploadDescriptionTextField.clear();
        uploadCategory.setValue(null);
        showAlert(Alert.AlertType.INFORMATION, "Fields Cleared", "The form has been reset.");
    }

    public void onSettingsBtnClick() throws IOException {
        Stage stage = (Stage) settingsBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("account-management.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void onProfileBtnClick() throws IOException {
        Stage stage = (Stage) profileBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("edit-profile.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }

    public void createCollectionBtn() throws IOException {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("createcollection-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
    }
}

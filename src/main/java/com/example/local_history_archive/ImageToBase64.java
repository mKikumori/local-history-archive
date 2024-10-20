package com.example.local_history_archive;

import java.util.Base64;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

/**
 * Class to convert a javafx Image into a base64 string
 */
public class ImageToBase64 {

    /**
     * Method for encoding a javafx Image into a base64 string
     * @param imagePath The path for the Image
     * @return Returns the base64 string, null otherwise
     */
    public static String encodeImageToBase64(String imagePath) {
        try {
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

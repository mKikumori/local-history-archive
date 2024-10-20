package com.example.local_history_archive;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.util.Base64;

/**
 * Class to convert a base64 string into a javafx Image
 */
public class Base64ToImage {

    /**
     * Method for converting a base64 string into a javafx Image
     * @param base64String The base64 string to be converted
     * @return Returns the javafx Image, null otherwise
     */
    public static Image base64ToImage(String base64String) {
        if (base64String == null || base64String.isEmpty()) {
            System.err.println("Base64 string is null or empty.");
            return null;
        }

        try {
            byte[] imageBytes = Base64.getDecoder().decode(base64String);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
            return new Image(inputStream);
        } catch (IllegalArgumentException e) {
            System.err.println("Failed to decode Base64 string: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("An error occurred while converting Base64 to Image: " + e.getMessage());
            return null;
        }
    }
}

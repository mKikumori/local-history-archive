package com.example.local_history_archive;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

public class Base64ToImage {
    public static Image base64ToImage(String base64String) {
        byte[] imageBytes = Base64.getDecoder().decode(base64String);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(imageBytes);
        return new Image(inputStream);
    }
}

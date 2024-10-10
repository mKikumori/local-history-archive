package com.example.local_history_archive.model;

import java.util.List;

public class SearchResult {

    private String type; // Type of search result (e.g., User, Collection, Upload)
    private int id; // ID of the result
    private List<String> result; // Contains upload_name, upload_description, etc.
    private String imageData; // To store image data or path
    private String uploadType; // To store the type of the upload

    public SearchResult(String type, int id, List<String> result, String imageData, String uploadType) {
        this.type = type;
        this.id = id;
        this.result = result;
        this.imageData = imageData;
        this.uploadType = uploadType;
    }

    // Getterss
    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public List<String> getResult() {
        return result;
    }

    public String getImageData() {
        return imageData;
    }

    public String getUploadType() {
        return uploadType; // Getter for upload type
    }

    @Override
    public String toString() {
        return "Type: " + type + ", ID: " + id + ", Result: " + result + ", Image Data: " + imageData + ", Upload Type: " + uploadType;
    }
}
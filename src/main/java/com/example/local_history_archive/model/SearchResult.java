package com.example.local_history_archive.model;

import java.util.List;

/**
 * A simple model class representing a user's search with an ID, search type, result, image data, and upload type.
 */
public class SearchResult {

    private String type;
    private int id;
    private List<String> result;
    private String imageData;
    private String uploadType;

    /**
     * Constructs a new SearchResult with an ID, search type, result, image data, and upload type.
     * @param type Type of search result (e.g., User, Collection, Upload)
     * @param id ID of the result
     * @param result Contains upload_name, upload_description, etc.
     * @param imageData To store image data or path
     * @param uploadType To store the type of the upload
     */
    public SearchResult(String type, int id, List<String> result, String imageData, String uploadType) {
        this.type = type;
        this.id = id;
        this.result = result;
        this.imageData = imageData;
        this.uploadType = uploadType;
    }

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
        return uploadType;
    }

    @Override
    public String toString() {
        return "Type: " + type + ", ID: " + id + ", Result: " + result + ", Image Data: " + imageData + ", Upload Type: " + uploadType;
    }
}
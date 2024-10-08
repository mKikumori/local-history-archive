package com.example.local_history_archive.model;

public class SearchResult {

    private String type;
    private int id;
    private String result;

    public SearchResult(String type, int id, String result) {
        this.type = type;
        this.id = id;
        this.result = result;
    }

    // Getters
    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "Type: " + type + "," + " ID: " + id + ", Result: " + result;
    }
}
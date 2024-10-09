package com.example.local_history_archive.model;

import java.util.ArrayList;
import java.util.List;

public class Collection {
    private int collection_id;
    private int creator_id;
    private String collection_name;
    private String created_at;
    private List<Integer> uploadIds;

    // Constructor to initialize all fields
    public Collection(int collection_id, int creator_id, String collection_name, String created_at) {
        this.collection_id = collection_id;
        this.creator_id = creator_id;
        this.collection_name = collection_name;
        this.created_at = created_at;
        this.uploadIds = new ArrayList<>();
    }

    public Collection(int creator_id, String collection_name, String created_at, List<Integer> upload_ids) {
        this.creator_id = creator_id;
        this.collection_name = collection_name;
        this.created_at = created_at;
        this.uploadIds = new ArrayList<>();
    }

    // Getters
    public int getCollection_id() {
        return collection_id;
    }

    public void addUpload(int uploadId) {
        uploadIds.add(uploadId);
    }

    public List<Integer> getUploadIds() {
        return uploadIds;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public String getCollection_name() {
        return collection_name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCollection_name(String collectionName) {
        this.collection_name = collectionName;
    }

    public void setCollectionId(int collectionId) {
        this.collection_id = collectionId;
    }
}

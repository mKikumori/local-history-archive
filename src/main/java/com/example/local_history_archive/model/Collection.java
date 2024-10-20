package com.example.local_history_archive.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple model class representing a user's collection with a collection ID, a creator ID, name, created at, and the upload IDs.
 */
public class Collection {
    private int collection_id;
    private int creator_id;
    private String collection_name;
    private String created_at;
    private List<Integer> uploadIds;

    /**
     * Constructs a new Collection with the specified collection ID, creator ID, collection name, and created at.
     * @param collection_id The ID of the collection
     * @param creator_id The ID of the user that created the collection
     * @param collection_name The name of the collection
     * @param created_at The time the collection was created
     */
    public Collection(int collection_id, int creator_id, String collection_name, String created_at) {
        this.collection_id = collection_id;
        this.creator_id = creator_id;
        this.collection_name = collection_name;
        this.created_at = created_at;
        this.uploadIds = new ArrayList<>();
    }

    /**
     * Constructs a new Collection with the specified collection ID, creator ID, collection name, created at, and upload IDs.
     * @param creator_id The ID of the creator of the collection
     * @param collection_name The name of the collection
     * @param created_at The time the collection was created
     * @param upload_ids The IDs of the uploads stored in the collection
     */
    public Collection(int creator_id, String collection_name, String created_at, List<Integer> upload_ids) {
        this.creator_id = creator_id;
        this.collection_name = collection_name;
        this.created_at = created_at;
        this.uploadIds = new ArrayList<>();
    }

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

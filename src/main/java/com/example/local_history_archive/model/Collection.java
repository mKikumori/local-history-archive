package com.example.local_history_archive.model;

public class Collection {
    private int collection_id;
    private int creator_id;
    private int upload_id;
    private String collection_name;
    private int shared_with;
    private String created_at;

    public Collection(int collection_id, int creator_id, int upload_id, String collection_name, int shared_with, String created_at) {
        this.collection_id = collection_id;
        this.creator_id = creator_id;
        this.upload_id = upload_id;
        this.collection_name = collection_name;
        this.shared_with = shared_with;
        this.created_at = created_at;
    }

    public int getCollection_id() {return collection_id;}
    public int getCreator_id() {return creator_id;}
    public int getUpload_id() {return upload_id;}
    public String getCollection_name() {
        return collection_name;
    }
    public int getShared_with() {
        return shared_with;
    }
    public String getCreated_at() {return created_at;}



}

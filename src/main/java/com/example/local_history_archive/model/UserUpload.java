package com.example.local_history_archive.model;

/**
 * A simple model class representing an upload with an ID, uploader ID, name, category, type, description, is pinned, image data, and uploaded at.
 */
public class UserUpload {

    private int upload_id;
    private int user_id;
    private String upload_name;
    private String upload_categories;
    private String upload_type;
    private String upload_description;
    private boolean is_pinned;
    private String image_data;
    private String uploadedAt;

    /**
     * Constructs a new UserUpload with an ID, uploader ID, name, category, type, description, is pinned, image data, and uploaded at.
     * @param upload_id The ID of the upload
     * @param user_id The ID of the uploader
     * @param upload_name The name of the upload
     * @param upload_categories The category of the upload
     * @param upload_type The type of upload
     * @param upload_description The description of the upload
     * @param is_pinned If the upload is pinned or not
     * @param image_data The image data of the upload
     * @param uploadedAt The time the upload was created
     */
    public UserUpload(int upload_id, int user_id, String upload_name, String upload_categories, String upload_type, String upload_description, boolean is_pinned, String image_data, String uploadedAt) {
        this.upload_id = upload_id;
        this.user_id = user_id;
        this.upload_name = upload_name;
        this.upload_categories = upload_categories;
        this.upload_type = upload_type;
        this.upload_description = upload_description;
        this.is_pinned = is_pinned;
        this.image_data = image_data;
        this.uploadedAt = uploadedAt;
    }

    /**
     * Constructs a new UserUpload with the uploader ID, name, category, type, description, is pinned, image data, and uploaded at.
     * @param user_id The ID of the uploader
     * @param upload_name The name of the upload
     * @param upload_categories The category of the upload
     * @param upload_type The type of upload
     * @param upload_description The description of the upload
     * @param is_pinned If the upload is pinned or not
     * @param image_data The image data of the upload
     * @param uploadedAt The time the upload was created
     */
    public UserUpload(int user_id, String upload_name, String upload_categories, String upload_type, String upload_description, boolean is_pinned, String image_data, String uploadedAt) {
        this.user_id = user_id;
        this.upload_name = upload_name;
        this.upload_categories = upload_categories;
        this.upload_type = upload_type;
        this.upload_description = upload_description;
        this.is_pinned = is_pinned;
        this.image_data = image_data;
        this.uploadedAt = uploadedAt;
    }


    public int getUploadId() {
        return upload_id;
    }

    public void setUploadId(int upload_id) {
        this.upload_id = upload_id;
    }

    public int getUserId() {
        return user_id;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public String getUploadName() {
        return upload_name;
    }

    public void setUploadName(String upload_name) {
        this.upload_name = upload_name;
    }

    public String getUploadCategories() {
        return upload_categories;
    }

    public void setUploadCategories(String upload_categories) {
        this.upload_categories = upload_categories;
    }

    public String getUploadType() {
        return upload_type;
    }

    public void setUploadType(String upload_type) {
        this.upload_type = upload_type;
    }

    public String getUploadDescription() {
        return upload_description;
    }

    public void setUploadDescription(String upload_description) {
        this.upload_description = upload_description;
    }

    public boolean isPinned() {
        return is_pinned;
    }

    public void setPinned(boolean pinned) {
        is_pinned = pinned;
    }

    public String getImageData() {
        return image_data;
    }

    public void setImageData(String image_data) {
        this.image_data = image_data;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }


    @Override
    public String toString() {
        return "UserUpload{" +
                "upload_id=" + upload_id +
                ", user_id=" + user_id +
                ", upload_name='" + upload_name + '\'' +
                ", upload_categories='" + upload_categories + '\'' +
                ", upload_type='" + upload_type + '\'' +
                ", upload_description='" + upload_description + '\'' +
                ", is_pinned=" + is_pinned + '\'' +
                ", image_data=" + image_data + '\'' +
                ", uploadedAt=" + uploadedAt +
                '}';
    }
}
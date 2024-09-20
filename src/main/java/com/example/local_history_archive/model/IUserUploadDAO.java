package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface IUserUploadDAO {
    public boolean userExists(int userId);
    public int getUserIdByUploadId(int uploadId);
    public void newUpload(UserUpload userUpload);
    public void updateUpload(UserUpload userUpload);
    public void deleteUpload(int upload_id);
    public List<UserUpload> allUploads();
    public UserUpload getUploadById(int upload_id);
    public void dropTable();
    public String getImageDataByUploadId(int uploadId);
}

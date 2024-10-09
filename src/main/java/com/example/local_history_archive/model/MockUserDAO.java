package com.example.local_history_archive.model;

import java.sql.SQLException;
import java.util.List;

public class MockUserDAO {
        // Create the database
        //UserAccountDAO userAccountDAO = new UserAccountDAO();
        //userAccountDAO.createTable();

        //String imagePath = "src/main/resources/images/johndoe.jpeg";
        //String profile_pic64 = ImageToBase64.encodeImageToBase64(imagePath);

        // Insert new users
        //userAccountDAO.newUser(new UserAccount("user@example.com", "john_doe", "secure_password", "Just a bio", profile_pic64));

        // Retrieve all records
        //List<UserAccount> accounts = userAccountDAO.getAll();
        //for (UserAccount acc : accounts) {
        //    System.out.println(acc);
        //}

        // Retrieve a record by ID
        //UserAccount account = userAccountDAO.getById(4);
        //System.out.println("Before update:");
        //System.out.println(account);

        // Update a record
        //System.out.println("After update:");
        //account.setBio("Lorem ipsum dolor sit amet, consectetur adipiscing elit, " +
        //        "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        //account.setUsername("John Doe");
        //account.setPassword("1234567");
        //userAccountDAO.updateUser(account);
        //System.out.println(userAccountDAO.getById(4));

        // Delete a record
        //System.out.println("Before deleting record with id = 1:");
        //for (UserAccount acc : userAccountDAO.getAll()) {
        //    System.out.println(acc);
        //}

        //userAccountDAO.deleteAccount(1);
        //System.out.println("After deleting record with id = 1:");
        //for (UserAccount acc : userAccountDAO.getAll()) {
        //    System.out.println(acc);
        //}

        //userAccountDAO.close();

    /*public static void main(String[] args) throws SQLException {
        UserUploadDAO userUploadDAO = new UserUploadDAO();

        // Create the uploads table
        userUploadDAO.createUploadTable();

        String imagePath = "src/main/resources/images/Colosseo_2020.jpg";
        String image_file = ImageToBase64.encodeImageToBase64(imagePath);

        // Insert new uploads
        //userUploadDAO.newUpload(new UserUpload(2, "Colosseo Image Upload", "Historic buildings", "image", "Description", false, image_file));
        //userUploadDAO.newUpload(new UserUpload(3, "First Upload", "Category3", "document", "Another description", true, ""));

        // Retrieve all uploads
        List<UserUpload> uploads = userUploadDAO.allUploads();
        System.out.println("All uploads:");
        for (UserUpload upload : uploads) {
            System.out.println(upload);
        }

        // Retrieve an upload by ID
        UserUpload uploadById = userUploadDAO.getUploadById(2);
        System.out.println("Upload with ID 2:");
        System.out.println(uploadById);

        // Update an upload
        if (uploadById != null) {
            uploadById.setUploadName("Updated Upload Name");
            uploadById.setUploadDescription("Updated description.");
            uploadById.setPinned(true);
            userUploadDAO.updateUpload(uploadById);

            System.out.println("After update:");
            System.out.println(userUploadDAO.getUploadById(2));
        }

        // Get user_id by upload_id
        int userId = userUploadDAO.getUserIdByUploadId(1);
        System.out.println("User ID: " + userId);

        // Delete an upload
        System.out.println("Before deleting upload with id = 2:");
        List<UserUpload> uploadsBeforeDelete = userUploadDAO.allUploads();
        for (UserUpload u : uploadsBeforeDelete) {
            System.out.println(u);
        }

        userUploadDAO.deleteUpload(2);

        System.out.println("After deleting upload with id = 2:");
        List<UserUpload> uploadsAfterDelete = userUploadDAO.allUploads();
        for (UserUpload u : uploadsAfterDelete) {
            System.out.println(u);
        }

        // Close the DAO
        //userUploadDAO.close();
    }*/
}

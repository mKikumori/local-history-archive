package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDAO {
    private Connection connection;

    public UserAccountDAO() {
        connection = DatabaseConnection.getInstance(); // DatabaseConnection is called
        createTable();
    }

    public void createTable() {
        try {
            Statement createTable = connection.createStatement();
            createTable.execute(
                    "CREATE TABLE IF NOT EXISTS userAccounts ("
                            + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "user_email TEXT NOT NULL, "
                            + "username TEXT NOT NULL, "
                            + " password TEXT NOT NULL,"
                            + " bio TEXT,"
                            + " profile_pic TEXT,"
                            + " created_at TEXT NOT NULL DEFAULT (datetime('now', 'localtime'))"
                            + ")"
            );
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    // Method for password recovery by email
    public String recoverPassword(String userEmail) {
        String password = null;

        String sql = "SELECT *  FROM userAccounts WHERE user_email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userEmail); // Bind the email to the query

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    password = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error recovering password: " + e.getMessage());
            e.printStackTrace();
        }

        return password;
    }

    //method for changing password
    public boolean changePassword(String userEmail, String newPassword) {
        String sql = "UPDATE userAccounts SET password = ? WHERE user_email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);  // Bind the new password
            pstmt.setString(2, userEmail);    // Bind the email

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;  // Return true if the password was successfully changed
        } catch (SQLException e) {
            System.out.println("Error changing password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    //Sign in
    public UserAccount signIn(String userEmail, String password) {
        UserAccount user = getByEmail(userEmail);  // Retrieve the user by email
        if (user != null && user.getPassword().equals(password)) {  // Check if the password matches
            return user;  // Successful sign-in
        }
        return null;  // Sign-in failed
    }

    // Email Duplicate Check
    public boolean isEmailDuplicate(String userEmail){
        try {
            PreparedStatement checkEmailStmt;
            checkEmailStmt = connection.prepareStatement("SELECT * FROM userAccounts WHERE user_email = ?");
            checkEmailStmt.setString(1, userEmail);
            ResultSet rs = checkEmailStmt.executeQuery();
            return rs.next();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return false;
    }

    // Username Duplicate Check
    public boolean isUsernameDuplicate(String username) {
        try {
            PreparedStatement checkUsernameStmt = connection.prepareStatement("SELECT * FROM userAccounts WHERE username = ?");
            checkUsernameStmt.setString(1, username);
            ResultSet rs = checkUsernameStmt.executeQuery();
            return rs.next();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return false;
    }

    // Valid username checking
    public boolean isValidUsername(String username) {
        // Only usernames that consist of alphabets, numbers, underscores (_), and hyphens (-) are allowed.
        String usernamePattern = "^[a-zA-Z0-9_-]{3,20}$";  // 3 characters or more and 20 characters or less
        return username.matches(usernamePattern);
    }

    // Checking email format
    public boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailPattern);
    }

    // Checking Email Duplicate while registration
    public boolean newUser(UserAccount userAccount) {

        // Check if required fields are empty
        if (userAccount.getUserEmail() == null || userAccount.getUserEmail().isEmpty()) {
            System.out.println("Email cannot be empty.");
            return false;
        }

        if (userAccount.getUsername() == null || userAccount.getUsername().isEmpty()) {
            System.out.println("Username cannot be empty.");
            return false;
        }

        if (userAccount.getPassword() == null || userAccount.getPassword().isEmpty()) {
            System.out.println("Password cannot be empty.");
            return false;
        }

        // Check if the username is invalid
        if (!isValidUsername(userAccount.getUsername())) {
            System.out.println("Invalid Username: " + userAccount.getUsername());
            return false;
        }

        // Check if the email is invalid
        if (!isValidEmail(userAccount.getUserEmail())) {
            System.out.println("Invalid Email: " + userAccount.getUserEmail());
            return false;
        }

        // Duplicate Email Checking
        if (isEmailDuplicate(userAccount.getUserEmail())) {
            System.out.println("There is duplicated Email: " + userAccount.getUserEmail());
            return false;
        }

        // Duplicate Username Checking
        if (isUsernameDuplicate(userAccount.getUsername())) {
            System.out.println("There is a duplicated Username: " + userAccount.getUsername());
            return false;
        }

        try {
            PreparedStatement newUserAccount = connection.prepareStatement(
                    "INSERT INTO userAccounts (user_email, username, password, bio, profile_pic, created_at) "
                            + "VALUES (?, ?, ?, ?, ?, datetime('now', 'localtime'))"
            );
            newUserAccount.setString(1, userAccount.getUserEmail());
            newUserAccount.setString(2, userAccount.getUsername());
            newUserAccount.setString(3, userAccount.getPassword());
            newUserAccount.setString(4, userAccount.getBio());
            newUserAccount.setString(5, userAccount.getProfilePic());

            // Add new user account to DB
            int rowsAffected = newUserAccount.executeUpdate();

            // If created successful (affected row = 1)
            if (rowsAffected > 0) {
                System.out.println("'" + userAccount.getUsername() + "'" + " account creation success.");
                return true;  // Account creation success
            }

        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return false; // Account creation failed
    }

    public void updateUser(UserAccount userAccount) {
        try {
            PreparedStatement updateAccount = connection.prepareStatement(
                    "UPDATE userAccounts SET user_email = ?, username = ?, password = ?, bio = ?, profile_pic = ? WHERE user_id = ?"
            );
            updateAccount.setString(1, userAccount.getUserEmail());
            updateAccount.setString(2, userAccount.getUsername());
            updateAccount.setString(3, userAccount.getPassword());
            updateAccount.setString(4, userAccount.getBio());
            updateAccount.setString(5, userAccount.getProfilePic());
            updateAccount.setInt(6, userAccount.getUserId());

            updateAccount.execute();

        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public void deleteAccount(int user_id) {
        try{
            PreparedStatement deleteAccount = connection.prepareStatement("DELETE FROM userAccounts WHERE user_id = ?");
            deleteAccount.setInt(1, user_id);

            deleteAccount.execute();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    public List<UserAccount> getAll() {
        List<UserAccount> accounts = new ArrayList<>();
        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT * FROM userAccounts");

            while (rs.next()) {
                accounts.add(
                        new UserAccount(
                                rs.getInt("user_id"),
                                rs.getString("user_email"),
                                rs.getString("username"),
                                rs.getString("password"),
                                rs.getString("bio"),
                                rs.getString("profile_pic")
                        )
                );
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return accounts;
    }

    public UserAccount getById(int user_id) {
        try {
            PreparedStatement getAccount = connection.prepareStatement("SELECT * FROM userAccounts WHERE user_id = ?");
            getAccount.setInt(1, user_id);
            ResultSet rs = getAccount.executeQuery();

            if (rs.next()) {
                return new UserAccount(
                        rs.getInt("user_id"),
                        rs.getString("user_email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("bio"),
                        rs.getString("profile_pic")
                );
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return null;
    }

    public UserAccount getByEmail(String user_email) {
        try {
            PreparedStatement getAccount = connection.prepareStatement("SELECT * FROM userAccounts WHERE user_email = ?");
            getAccount.setString(1, user_email);
            ResultSet rs = getAccount.executeQuery();

            if (rs.next()) {
                return new UserAccount(
                        rs.getInt("user_id"),
                        rs.getString("user_email"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("bio"),
                        rs.getString("profile_pic")
                );
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return null;
    }

    // For Testing
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM userAccounts WHERE user_email = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }
}

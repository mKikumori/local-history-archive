package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A DAO class for the database operations regarding the user accounts
 */
public class UserAccountDAO {
    private Connection connection;

    public UserAccountDAO() {
        connection = DatabaseConnection.getInstance();
        createTable();
    }

    /**
     * Creates the userAccounts table if not exists
     */
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

    /**
     * Method for password recovery by email
     * @param userEmail The user's email
     * @return Returns the password for the given email
     */
    public String recoverPassword(String userEmail) {
        String password = null;

        String sql = "SELECT *  FROM userAccounts WHERE user_email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userEmail);

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

    /**
     * Method for changing password for a given email
     * @param userEmail The email whose password to change
     * @param newPassword The new password for the given email
     * @return Returns true if the password was successfully changed, false otherwise
     */
    public boolean changePassword(String userEmail, String newPassword) {
        String sql = "UPDATE userAccounts SET password = ? WHERE user_email = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, userEmail);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Error changing password: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method for signing in
     * @param userEmail The email of the user
     * @param password The password of the user
     * @return Returns the user's details, null otherwise
     */
    public UserAccount signIn(String userEmail, String password) {
        UserAccount user = getByEmail(userEmail);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    /**
     * Method for checking if there is an email duplicate
     * @param userEmail The email to be registered
     * @return Returns the duplicate, false otherwise
     */
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

    /**
     * Method for checking if there is a username duplicate
     * @param username The username to be checked
     * @return Returns the duplicate, false otherwise
     */
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

    /**
     * Method to check if the username is valid
     * @param username The username to be checked
     * @return Returns true if the username is valid, false otherwise
     */
    public boolean isValidUsername(String username) {
        // Only usernames that consist of alphabets, numbers, underscores (_), and hyphens (-) are allowed.
        String usernamePattern = "^[a-zA-Z0-9_-]{3,20}$";  // 3 characters or more and 20 characters or less
        return username.matches(usernamePattern);
    }

    /**
     * Method to check if the email is valid
     * @param email The email to be checked
     * @return Returns true if the email is valid, false otherwise
     */
    public boolean isValidEmail(String email) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email.matches(emailPattern);
    }

    /**
     * Method for creating a new user
     * @param userAccount The registering user's details
     * @return Returns true if successful, false otherwise
     */
    public boolean newUser(UserAccount userAccount) {

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

        if (!isValidUsername(userAccount.getUsername())) {
            System.out.println("Invalid Username: " + userAccount.getUsername());
            return false;
        }

        if (!isValidEmail(userAccount.getUserEmail())) {
            System.out.println("Invalid Email: " + userAccount.getUserEmail());
            return false;
        }

        if (isEmailDuplicate(userAccount.getUserEmail())) {
            System.out.println("There is duplicated Email: " + userAccount.getUserEmail());
            return false;
        }

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

            int rowsAffected = newUserAccount.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("'" + userAccount.getUsername() + "'" + " account creation success.");
                return true;
            }
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
        return false;
    }

    /**
     * Method ofr updating a user's details
     * @param userAccount The details to be updated
     */
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

    /**
     * Method for deleting a user given a user ID
     * @param user_id The user ID to be deleted
     */
    public void deleteAccount(int user_id) {
        try{
            PreparedStatement deleteAccount = connection.prepareStatement("DELETE FROM userAccounts WHERE user_id = ?");
            deleteAccount.setInt(1, user_id);

            deleteAccount.execute();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

    /**
     * Method for retuning all users in the database
     * @return Returns all user accounts on the database
     */
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

    /**
     * Method for getting a user given a user ID
     * @param user_id The user ID to be searched
     * @return Returns the user matching the given ID, null otherwise
     */
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

    /**
     * Method for getting a user given an email
     * @param user_email The email to be searched
     * @return Returns the user matching the given email, null otherwise
     */
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

    /**
     * Method for deleting a user given a email
     * @param email The email of the user to be deleted
     */
    public void deleteByEmail(String email) {
        String sql = "DELETE FROM userAccounts WHERE user_email = ?";

        try (PreparedStatement pstmt = this.connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

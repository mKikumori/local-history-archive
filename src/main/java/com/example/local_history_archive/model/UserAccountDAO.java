package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserAccountDAO {
    private Connection connection;

    public UserAccountDAO() {
        connection = DatabaseConnection.getInstance();
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

    public void newUser(UserAccount userAccount) {
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

            newUserAccount.execute();

        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
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

    public void close() {
        try {
            connection.close();
        } catch (SQLException SQLEx) {
            System.err.println(SQLEx);
        }
    }

}

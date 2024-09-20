package com.example.local_history_archive.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface IUserAccountDAO {
    public void newUser(UserAccount userAccount);
    public void updateUser(UserAccount userAccount);
    public void deleteAccount(int user_id);
    public List<UserAccount> getAll();
    public UserAccount getById(int user_id);
    public UserAccount getByEmail(String user_email);
    public void close();
}

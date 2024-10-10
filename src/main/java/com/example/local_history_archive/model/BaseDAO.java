package com.example.local_history_archive.model;


import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BaseDAO {

    private Connection connection;

    public BaseDAO() {
        connection = DatabaseConnection.getInstance();
    }




}
package com.example.local_history_archive.model;

public class SessionManager {
    private static UserAccount currentUser;

    public static void setCurrentUser(UserAccount user) {
        currentUser = user;
    }

    public static UserAccount getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }
}

package com.example.local_history_archive.model;

/**
 * A class to manage the current session
 */
public class SessionManager {
    private static UserAccount currentUser;

    /**
     * Method to set the current logged-in user
     * @param user Updates the currentUser with the legged-in user's details
     */
    public static void setCurrentUser(UserAccount user) {
        currentUser = user;
    }

    /**
     * Method to get the currentUser
     * @return Returns the currentUser's details
     */
    public static UserAccount getCurrentUser() {
        return currentUser;
    }

    /**
     * Method to clear the session and the currentUser
     */
    public static void clearSession() {
        currentUser = null;
    }
}

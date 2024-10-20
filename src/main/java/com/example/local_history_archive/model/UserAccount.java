package com.example.local_history_archive.model;

/**
 * A simple model class representing a user account with an ID, username, password, email, bio, and profile picture.
 */
public class UserAccount {

    private int user_id;
    private String username;
    private String password;
    private String user_email;
    private String bio;
    private String profile_pic;

    /**
     * Constructs a new UserAccount with the specified ID, email, username, password, bio, and profile picture.
     * @param user_id The ID of the user
     * @param user_email The email of the user
     * @param username The username of the user
     * @param password The password of the user
     * @param bio The bio of the user
     * @param profile_pic The profile picture of the user
     */
    public UserAccount(int user_id, String user_email, String username, String password, String bio, String profile_pic) {
        this.user_id = user_id;
        this.user_email = user_email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.profile_pic = profile_pic;
    }

    /**
     * Constructs a new UserAccount with the specified email, username, password, bio, and profile picture.
     * @param user_email The email of the user
     * @param username The username of the user
     * @param password The password of the user
     * @param bio The bio of the user
     * @param profile_pic The profile picture of the user
     */
    public UserAccount(String user_email, String username, String password, String bio, String profile_pic) {
        this.user_email = user_email;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.profile_pic = profile_pic;
    }

    public int getUserId() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUserEmail() {
        return user_email;
    }

    public String getBio() {
        return bio;
    }

    public String getProfilePic() {
        return profile_pic;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserEmail(String user_email) {
        this.user_email = user_email;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setProfilePic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    @Override
    public String toString() {
        return "UserAccount{" +
                "user_id=" + user_id +
                ", user_email='" + user_email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", profile_pic='" + profile_pic + '\'' +
                '}';
    }
}

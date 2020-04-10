package fr.aureliejosephine.go4lunch.models;


import androidx.annotation.Nullable;



public class User {

    private String uid;
    private String username;
    @Nullable
    private String picture;
    private String email;
    private String restaurantName;

    public User() { }

    public User(String uid, String username, String urlPicture, String uEmail) {
        this.uid = uid;
        this.username = username;
        this.picture = urlPicture;
        this.email = uEmail;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getPicture() { return picture; }
    public String getEmail() {
        return email;
    }
    public String getRestaurantName() {
        return restaurantName;
    }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setPicture(String picture) { this.picture = picture; }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}

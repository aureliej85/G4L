package fr.aureliejosephine.go4lunch.models;


import androidx.annotation.Nullable;



public class User {

    private String uid;
    private String username;
    @Nullable
    private String picture;
    private String email;
    private String placeId;
    private String placeName;

    public User() { }

    public User(String uid, String username,String uEmail) {
        this.uid = uid;
        this.username = username;
        this.email = uEmail;
    }

    public User(String uid, String username, String picture, String uEmail) {
        this.uid = uid;
        this.username = username;
        this.picture = picture;
        this.email = uEmail;
    }

    public User(String uid, String username, String picture, String uEmail, String placeId, String placeName) {
        this.uid = uid;
        this.username = username;
        this.picture = picture;
        this.email = uEmail;
        this.placeId = placeId;
        this.placeName = placeName;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getPicture() { return picture; }
    public String getEmail() {
        return email;
    }
    public String getPlaceId() {
        return placeId;
    }
    public String getPlaceName() {
        return placeName;
    }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setPicture(String picture) { this.picture = picture; }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }
}

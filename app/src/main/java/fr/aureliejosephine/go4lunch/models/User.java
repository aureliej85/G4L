package fr.aureliejosephine.go4lunch.models;


import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;



public class User {

    private String uid;
    private String username;
    @Nullable
    private String picture;
    private String email;
    private String placeId;
    private String placeName;
    private List<String> restaurantsLiked;
    private double latitude;
    private double longitude;
    private String date;

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

    public User(String uid, String username, String picture, String uEmail, String placeId, String placeName, List<String> restaurantsLiked, Double latitude, Double longitude, String date) {
        this.uid = uid;
        this.username = username;
        this.picture = picture;
        this.email = uEmail;
        this.placeId = placeId;
        this.placeName = placeName;
        this.restaurantsLiked = restaurantsLiked;
        this.latitude = latitude;
        this.longitude = longitude;
        this.date = date;
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
    public List<String> getRestaurantsLiked() {
        return restaurantsLiked;
    }
    public double getLatitude() {
        return latitude;
    }
    public double getLongitude() {
        return longitude;
    }
    public String getDate() {
        return date;
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
    public void setRestaurantsLiked(List<String> restaurantsLiked) {
        this.restaurantsLiked = restaurantsLiked;
    }
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
    public void setDate(String date) {
        this.date = date;
    }
}

package fr.aureliejosephine.go4lunch.models;


import androidx.annotation.Nullable;

/**
 * Created by Philippe on 30/01/2018.
 */

public class User {

    private String uid;
    private String username;
    @Nullable
    private String urlPicture;
    private String uEmail;

    public User() { }

    public User(String uid, String username, String urlPicture, String uEmail) {
        this.uid = uid;
        this.username = username;
        this.urlPicture = urlPicture;
        this.uEmail = uEmail;
    }

    // --- GETTERS ---
    public String getUid() { return uid; }
    public String getUsername() { return username; }
    public String getUrlPicture() { return urlPicture; }
    public String getuEmail() {
        return uEmail;
    }

    // --- SETTERS ---
    public void setUsername(String username) { this.username = username; }
    public void setUid(String uid) { this.uid = uid; }
    public void setUrlPicture(String urlPicture) { this.urlPicture = urlPicture; }
    public void setuEmail(String uEmail) {
        this.uEmail = uEmail;
    }
}

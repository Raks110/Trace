package com.rakshitgl.trace.models;

import android.net.Uri;

public class User {

    private String email;
    private String displayName;
    private String photoURL;

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}

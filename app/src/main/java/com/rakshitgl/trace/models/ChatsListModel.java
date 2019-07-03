package com.rakshitgl.trace.models;

import java.util.Date;

public class ChatsListModel {

    private String email;
    private String displayName;
    private String photoURL;
    private String lastMessage;
    private long lastSeen;
    private Date lastMessageReceived;

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public Date getLastMessageReceived() {
        return lastMessageReceived;
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

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public void setLastMessageReceived(Date lastMessageReceived) {
        this.lastMessageReceived = lastMessageReceived;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }
}

package com.rakshitgl.trace.models;

import java.util.List;

public class UserListMonetary {

    private String displayName;
    private String email;
    private String photoUrl;
    private long maxMoney;
    private List<MonetaryUnit> list;

    public String getDisplayName() {
        return displayName;
    }

    public List<MonetaryUnit> getList() {
        return list;
    }

    public long getMaxMoney() {
        return maxMoney;
    }

    public String getEmail() {
        return email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setList(List<MonetaryUnit> list) {
        this.list = list;
    }

    public void setMaxMoney(long maxMoney) {
        this.maxMoney = maxMoney;
    }
}

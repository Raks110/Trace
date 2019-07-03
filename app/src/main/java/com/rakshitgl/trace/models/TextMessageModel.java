package com.rakshitgl.trace.models;

import java.util.Date;

public class TextMessageModel {

    private Date timestamp;
    private String senderEmail;
    private String text;
    private boolean moneyStatus;
    private String currency;
    private int money;

    public Date getTimestamp() {
        return timestamp;
    }

    public String getText() {
        return text;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public int getMoney() {
        return money;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean getMoneyStatus(){
        return moneyStatus;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setMoneyStatus(boolean moneyStatus) {
        this.moneyStatus = moneyStatus;
    }
}

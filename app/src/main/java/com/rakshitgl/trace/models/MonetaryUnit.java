package com.rakshitgl.trace.models;

public class MonetaryUnit {

    private long money;
    private String currency;

    public String getCurrency() {
        return currency;
    }

    public long getMoney() {
        return money;
    }

    public void setMoney(long money) {
        this.money = money;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

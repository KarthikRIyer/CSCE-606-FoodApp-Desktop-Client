package com.foodapp.model;

public class Bill {
    private int orderId;
    private String name;
    private String address;
    private long phoneNumber;
    private long cardNumber;
    private int cvv;
    private int fromMM;
    private int fromYYYY;
    private int toMM;
    private int toYYYY;
    private double amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public void setCvv(int cvv) {
        this.cvv = cvv;
    }

    public int getFromMM() {
        return fromMM;
    }

    public void setFromMM(int fromMM) {
        this.fromMM = fromMM;
    }

    public int getFromYYYY() {
        return fromYYYY;
    }

    public void setFromYYYY(int fromYYYY) {
        this.fromYYYY = fromYYYY;
    }

    public int getToMM() {
        return toMM;
    }

    public void setToMM(int toMM) {
        this.toMM = toMM;
    }

    public int getToYYYY() {
        return toYYYY;
    }

    public void setToYYYY(int toYYYY) {
        this.toYYYY = toYYYY;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getOrderId() { return orderId; }

    public void setOrderId(int orderId) { this.orderId = orderId; }
}

package com.example.payment;

public class Transaction {
    private String receiverName;
    private double amount;
    private String receiverUid;
    private String senderUid;
    private long timestamp;
    String fieldName;
    String receiverProfileImg,senderProfileImg;


    public Transaction(double amount, String receiverUid, String senderUid, long timestamp, String fieldName, String receiverProfileImg,String senderProfileImg) {
        this.amount = amount;
        this.receiverUid = receiverUid;
        this.senderUid = senderUid;
        this.timestamp = timestamp;
        this.receiverName = receiverName;
        this.fieldName=fieldName;
        this.receiverProfileImg = receiverProfileImg;
        this.senderProfileImg = senderProfileImg;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(String receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(String senderUid) {
        this.senderUid = senderUid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setReceiverProfileImg(String receiverProfileImg) {
        this.receiverProfileImg = receiverProfileImg;

    }

    public String getReceiverProfileImg() {
        return receiverProfileImg;
    }

    public void setSenderProfileImg(String senderProfileImg) {
        this.senderProfileImg = senderProfileImg;
    }

    public String getSenderProfileImg() {
        return senderProfileImg;
    }
}

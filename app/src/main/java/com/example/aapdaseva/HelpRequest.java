package com.example.aapdaseva;

public class HelpRequest {
    private String message;
    private String senderId; // Add senderId field

    // Default constructor required for Firestore
    public HelpRequest() {
    }

    public HelpRequest(String message, String senderId) {
        this.message = message;
        this.senderId = senderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}

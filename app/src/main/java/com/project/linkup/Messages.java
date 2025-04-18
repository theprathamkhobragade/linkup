package com.project.linkup;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class Messages implements Serializable {
    private String message;
    private String senderId;
    private String receiverId;
    private String timestamp;

    // Default constructor (required for Firebase)
    public Messages() {
    }

    // Parameterized constructor
    public Messages(String message, String senderId, String receiverId, String timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    // Getter and Setter methods
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

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setFormattedTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    // Optional: Exclude from Firebase storage
    @Exclude
    public String getFormattedMessage() {
        return senderId + ": " + message;
    }

    @NonNull
    @Override
    public String toString() {
        return "Messages{" +
                "message='" + message + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}

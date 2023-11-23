package com.example.chatroom.dataClasses;

public class InputtedData {
    private String message;
    private String Address;
    private int port = 0;
    private String username;
    private String privateMessageReceiverUsername;
    private String newUsername;
    private String passwordToBeChecked;


    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return Address;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPrivateMessageReceiverUsername() {
        return privateMessageReceiverUsername;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public String getPasswordToBeChecked() {
        return passwordToBeChecked;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPrivateMessageReceiverUsername(String privateMessageReceiverUsername) {
        this.privateMessageReceiverUsername = privateMessageReceiverUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public void setPasswordToBeChecked(String passwordToBeChecked) {
        this.passwordToBeChecked = passwordToBeChecked;
    }
}

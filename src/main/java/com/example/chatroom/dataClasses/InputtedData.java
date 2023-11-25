package com.example.chatroom.dataClasses;

public class InputtedData {
    private String message;
    private int port = 0;
    private String username;


    public String getMessage() {
        return message;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

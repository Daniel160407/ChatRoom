package com.example.chatroom.dataClasses;

public class InputtedData {
    private String message;
    private String Address;
    private int port = 0;


    public String getMessage() {
        return message;
    }

    public String getAddress() {
        return Address;
    }

    public int getPort() {
        return port;
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
}

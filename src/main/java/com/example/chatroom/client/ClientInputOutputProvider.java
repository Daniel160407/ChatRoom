package com.example.chatroom.client;

import com.example.chatroom.controllers.HomeController;
import com.example.chatroom.controllers.ConnectController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientInputOutputProvider {
    private final ConnectController connectController;
    private final HomeController homeController;

    public ClientInputOutputProvider(ConnectController connectController, HomeController homeController) {
        this.connectController = connectController;
        this.homeController = homeController;
    }


}

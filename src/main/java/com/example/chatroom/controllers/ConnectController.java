package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.net.ConnectException;

public class ConnectController {
    @FXML
    private TextField addressField;
    @FXML
    private TextField portField;
    @FXML
    public Label notificationLabel;


    public ConnectController connectController;
    public Client client;


    @FXML
    private void onConnectButtonAction() {
        enterRequest();
    }


    @FXML
    private void onPortFieldAction() {
        enterRequest();
    }

    private void enterRequest() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 497, 733);
            HomeController homeController = fxmlLoader.getController();
            homeController.connectController = this.connectController;
            client = new Client(addressField.getText(), Integer.parseInt(portField.getText()), homeController);
            if (client.getSocket() != null) {
                addressField.getScene().getWindow().hide();
                Stage stage = new Stage();
                stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
                stage.setTitle("ChatRoom");
                stage.setScene(scene);
                stage.show();
                stage.setOnCloseRequest(event -> {
                    exitRequest();
                    System.exit(0);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitRequest() {
        client.sendSpecificMessage("#deleteMember#: " + client.homeController.getInputtedData().getUsername());
    }
}


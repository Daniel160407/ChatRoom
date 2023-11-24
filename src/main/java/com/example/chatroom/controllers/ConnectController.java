package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.client.Client;
import com.example.chatroom.client.ClientInputOutputProvider;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;

public class ConnectController {
    @FXML
    private TextField addressField;
    @FXML
    private TextField portField;


    public ConnectController connectController;
    private HomeController homeController;
    public Client client;
    public ClientInputOutputProvider clientInputOutputProvider;


    @FXML
    private void onConnectButtonAction() {
        enterRequest();
    }


    @FXML
    private void onPortFieldAction() {
        enterRequest();
    }

    private void enterRequest() {
        addressField.getScene().getWindow().hide();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 497, 733);
            Stage stage = new Stage();
            stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
            stage.setTitle("ChatRoom");
            stage.setScene(scene);
            stage.show();
            homeController = fxmlLoader.getController();
            System.out.println("Entered");
            client = new Client(addressField.getText(), Integer.parseInt(portField.getText()), homeController);
            homeController.connectController = this.connectController;
            stage.setOnCloseRequest(event -> exitRequest());
            clientInputOutputProvider = new ClientInputOutputProvider(connectController, client.homeController);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exitRequest() {
        System.out.println("Exit");
        client.sendSpecificMessage("#deleteMember#: " + client.homeController.getInputtedData().getUsername());
    }
}


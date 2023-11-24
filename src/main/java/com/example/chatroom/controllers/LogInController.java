package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.client.Client;
import com.example.chatroom.client.ClientInputOutputProvider;
import com.example.chatroom.dataClasses.PersonalData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogInController {
    @FXML
    private TextField userNameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField portField;


    public boolean enterPermission;
    public PersonalData registeredPeople = new PersonalData();
    public RegisterController registerController;
    public LogInController logInController;
    private HomeController homeController;
    public Client client;
    public ClientInputOutputProvider clientInputOutputProvider;


    @FXML
    private void onConnectButtonAction() {
        enterRequest();
    }

    @FXML
    private void onRegisterMouseClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        registerController = fxmlLoader.getController();
        registerController.logInController = logInController;
        Stage stage = new Stage();
        stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    private void onPortFieldAction() {
        System.out.println("Ura");
        enterRequest();
    }

    private void enterRequest() {
        for (int i = 0; i < registeredPeople.data.size(); i++) {
            if (registeredPeople.data.get(i).get(2).equals(userNameField.getText())
                    && registeredPeople.data.get(i).get(1).equals(passwordField.getText())) {
                enterPermission = true;
                break;
            } else {
                enterPermission = false;
            }
        }
        System.out.println(enterPermission);
        System.out.println(registeredPeople.data);
        if (enterPermission) {
            userNameField.getScene().getWindow().hide();
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
                homeController.getInputtedData().setUsername(userNameField.getText());
                client.sendSpecificMessage("#startup#: " + userNameField.getText());
                homeController.logInController = this.logInController;
                stage.setOnCloseRequest(event -> clientInputOutputProvider.exitRequest());
                clientInputOutputProvider = new ClientInputOutputProvider(logInController, client.homeController);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

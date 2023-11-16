package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.client.Client;
import com.example.chatroom.dataClasses.PersonalData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LogInController {
    @FXML
    private TextField userNameField;
    public boolean enterPermission;
    public PersonalData registeredPeople = new PersonalData();
    public RegisterController registerController;
    public LogInController logInController;

    @FXML
    private void onUserNameFieldAction() throws IOException {
        System.out.println(registeredPeople.data);
        for (int i = 0; i < registeredPeople.data.size(); i++) {
            if (registeredPeople.data.get(i).get(2).equals(userNameField.getText())) {
                enterPermission = true;
                break;
            } else {
                enterPermission = false;
            }
        }
        if (enterPermission) {
            userNameField.getScene().getWindow().hide();
            FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 497, 733);
            Stage stage = new Stage();
            stage.setTitle("ChatRoom");
            stage.setScene(scene);
            stage.show();
            HomeController homeController = fxmlLoader.getController();
            Client client = new Client(homeController);
            stage.setOnCloseRequest(event -> {
                try {
                    FileWriter fileWriter = new FileWriter("src/main/resources/com/example/chatroom/txt files/personalData.txt",true);
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    for (int i = 0; i < registeredPeople.data.size(); i++) {
                        for (int j = 0; j < registeredPeople.data.get(i).size(); j++) {
                            bufferedWriter.write(registeredPeople.data.get(i).get(j));
                            bufferedWriter.newLine();
                        }
                    }
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @FXML
    private void onRegisterMouseClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        registerController = fxmlLoader.getController();
        registerController.logInController = logInController;
        Stage stage = new Stage();
        stage.setTitle("Register");
        stage.setScene(scene);
        stage.show();
    }
}

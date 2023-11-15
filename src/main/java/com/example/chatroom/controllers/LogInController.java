package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LogInController {
    @FXML
    private TextField userNameField;
    public boolean enterPermission;

    @FXML
    private void onUserNameFieldAction() throws IOException {
        FileReader fileReader = new FileReader("src/main/resources/com/example/chatroom/txt files/personalData.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        List<List<String>> registeredPeople = new ArrayList<>();
        List<String> person = new ArrayList<>();
        String str;
        int j = 1;
        while ((str = bufferedReader.readLine()) != null) {
            person.add(str);
            if (j % 3 == 0) {
                registeredPeople.add(person);
                person.clear();
                j = 0;
            }
            j++;
        }
        for (int i = 0; i < registeredPeople.size(); i++) {
            if (registeredPeople.get(i).get(2).equals(userNameField.getText())) {
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
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
            HomeController homeController = fxmlLoader.getController();
            Client client = new Client(homeController);
        }
    }

    @FXML
    private void onRegisterMouseClicked() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        Stage stage = new Stage();
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }
}

package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.dataClasses.PersonalData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterController extends LogInController {
    @FXML
    private Label errorMessage;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField userNameField;
    private boolean registerPermission;
    public LogInController logInController;



    @FXML
    private void onSaveButtonAction() {
        registerPermission = logInController.registeredPeople.data.isEmpty();
        System.out.println(logInController.registeredPeople.data);
        if (!registerPermission) {
            System.out.println("1");
            for (int i = 0; i < logInController.registeredPeople.data.size(); i++) {
                if (logInController.registeredPeople.data.get(i).get(0).equals(emailField.getText()) ||
                        logInController.registeredPeople.data.get(i).get(2).equals(userNameField.getText())) {
                    registerPermission = false;
                    System.out.println("false");
                    break;
                } else {
                    registerPermission = true;
                    System.out.println("true");
                }
            }
        }

        if (registerPermission) {
            List<String> personData = new ArrayList<>();
            personData.add(emailField.getText());
            personData.add(passwordField.getText());
            personData.add(userNameField.getText());
            logInController.registeredPeople.data.add(personData);
            emailField.getScene().getWindow().hide();
        } else {
            errorMessage.setText("User with the same email or username already registered!");
        }

    }
}

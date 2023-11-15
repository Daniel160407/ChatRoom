package com.example.chatroom.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class RegisterController {
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField userNameField;

    @FXML
    private void onEmailFieldAction() {

    }

    @FXML
    private void onPasswordFieldAction() {

    }

    @FXML
    private void onUserNameFieldAction() {

    }

    @FXML
    private void onSaveButtonAction() throws IOException {
        FileWriter fileWriter = new FileWriter("src/main/resources/com/example/chatroom/txt files/personalData.txt");
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(emailField.getText());
        bufferedWriter.newLine();
        bufferedWriter.write(passwordField.getText());
        bufferedWriter.newLine();
        bufferedWriter.write(userNameField.getText());
        bufferedWriter.newLine();
        bufferedWriter.close();
    }
}

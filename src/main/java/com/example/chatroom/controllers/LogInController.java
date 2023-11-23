package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.client.Client;
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


    public boolean enterPermission;
    public PersonalData registeredPeople = new PersonalData();
    public RegisterController registerController;
    public LogInController logInController;
    public Client client;


    @FXML
    private void onPasswordFieldAction() throws IOException {
        enterRequest();
    }

    @FXML
    private void onLogInButtonAction() throws IOException {
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

    private void enterRequest() throws IOException {
        for (int i = 0; i < registeredPeople.data.size(); i++) {
            if (registeredPeople.data.get(i).get(2).equals(userNameField.getText())
                    && registeredPeople.data.get(i).get(1).equals(passwordField.getText())) {
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
            stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
            stage.setTitle("ChatRoom");
            stage.setScene(scene);
            stage.show();
            HomeController homeController = fxmlLoader.getController();
            client = new Client(homeController);
            homeController.getInputtedData().setUsername(userNameField.getText());
            client.sendSpecificMessage("#startup#: " + userNameField.getText());
            homeController.logInController = this.logInController;
            stage.setOnCloseRequest(event -> {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/com/example/chatroom/txt files/personalData.txt"));
                    List<List<String>> fileData = new ArrayList<>();
                    List<String> data = new ArrayList<>();
                    String str;
                    while ((str = bufferedReader.readLine()) != null) {
                        data.add(str);
                    }
                    for (int i = 2; i < data.size(); i += 3) {
                        List<String> sortedData = new ArrayList<>();
                        sortedData.add(data.get(i - 2));
                        sortedData.add(data.get(i - 1));
                        sortedData.add(data.get(i));
                        fileData.add(sortedData);
                    }
                    for (int i = 0; i < fileData.size(); i++) {
                        if (!registeredPeople.data.contains(fileData.get(i))) {
                            registeredPeople.data.add(fileData.get(i));
                        }
                    }
                    bufferedReader.close();
                    System.out.println("Sizes: "+logInController.registeredPeople.data.size());
                    for (int i = 0; i < logInController.registeredPeople.data.size() - 1; i++) {
                        for (int j = i + 1; j < logInController.registeredPeople.data.size(); j++) {
                            if (logInController.registeredPeople.data.get(i).get(0).equals(logInController.registeredPeople.data.get(j).get(0))) {
                                System.out.println("Registered people:");
                                System.out.println(logInController.registeredPeople.data.get(i).get(0));
                                System.out.println(logInController.registeredPeople.data.get(j).get(0));
                                logInController.registeredPeople.data.remove(j);
                            }
                        }
                    }
                    FileWriter fileWriter = new FileWriter("src/main/resources/com/example/chatroom/txt files/personalData.txt");
                    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                    for (int i = 0; i < registeredPeople.data.size(); i++) {
                        for (int j = 0; j < registeredPeople.data.get(i).size(); j++) {
                            if (registeredPeople.data.get(i).get(j) != null) {
                                bufferedWriter.write(registeredPeople.data.get(i).get(j));
                                bufferedWriter.newLine();
                            }
                        }
                    }
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                client.sendSpecificMessage("#deleteMember#: " + homeController.getInputtedData().getUsername());
            });
        }
    }
}

package com.example.chatroom;

import com.example.chatroom.client.Client;
import com.example.chatroom.controllers.HomeController;
import com.example.chatroom.controllers.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/logIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        LogInController logInController = fxmlLoader.getController();
        logInController.logInController = fxmlLoader.getController();
        stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
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
                    if (!logInController.registeredPeople.data.contains(fileData.get(i))) {
                        logInController.registeredPeople.data.add(fileData.get(i));
                    }
                }
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileWriter fileWriter = new FileWriter("src/main/resources/com/example/chatroom/txt files/personalData.txt");
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < logInController.registeredPeople.data.size(); i++) {
                    for (int j = 0; j < logInController.registeredPeople.data.get(i).size(); j++) {
                        bufferedWriter.write(logInController.registeredPeople.data.get(i).get(j));
                        bufferedWriter.newLine();
                    }
                }
                bufferedWriter.close();
                fileWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        FileReader fileReader = new FileReader("src/main/resources/com/example/chatroom/txt files/personalData.txt");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String str;
        List<String> allData = new ArrayList<>();
        while ((str = bufferedReader.readLine()) != null) {
            allData.add(str);
        }
        for (int i = 2; i < allData.size(); i += 3) {
            List<String> data = new ArrayList<>();
            data.add(allData.get(i - 2));
            data.add(allData.get(i - 1));
            data.add(allData.get(i));
            logInController.registeredPeople.data.add(data);
        }
        bufferedReader.close();
        fileReader.close();
    }

    public static void main(String[] args) {
        launch();
    }
}
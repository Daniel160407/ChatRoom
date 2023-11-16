package com.example.chatroom;

import com.example.chatroom.client.Client;
import com.example.chatroom.controllers.HomeController;
import com.example.chatroom.controllers.LogInController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> {
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
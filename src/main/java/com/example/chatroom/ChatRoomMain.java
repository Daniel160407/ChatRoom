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
        stage.setTitle("ChatRoom");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> logInController.clientInputOutputProvider.exitRequest());
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
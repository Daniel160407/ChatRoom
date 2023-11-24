package com.example.chatroom;

import com.example.chatroom.controllers.ConnectController;
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
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/connect.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        ConnectController connectController = fxmlLoader.getController();
        connectController.connectController = fxmlLoader.getController();
        stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
        stage.setTitle("ChatRoom");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> connectController.exitRequest());
    }

    public static void main(String[] args) {
        launch();
    }
}
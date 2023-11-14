package com.example.chatroom;

import com.example.chatroom.client.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ChatRoomMain extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        User user=new User();
    }

    public static void main(String[] args) {
        launch();
    }
}
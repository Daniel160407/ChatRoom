package com.example.chatroom.controllers;

import com.example.chatroom.dataClasses.InputtedData;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ServerController extends Application {

    @FXML
    private TextField port;
    @FXML
    public Label serverMessage;
    @FXML
    public AnchorPane mainAnchorPane;
    public InputtedData inputtedData = new InputtedData();
    private int previousMessageYPath = 27;

    @FXML
    private void onPortAction() {
        inputtedData.setPort(Integer.parseInt(port.getText()));
    }

    public void addNotificationText(String notificationText) {
        Text text = new Text();
        text.setWrappingWidth(450);
        text.setStyle("-fx-font: 20px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
        text.setText(notificationText);
        text.setLayoutX(14);
        text.setLayoutY(previousMessageYPath);
        previousMessageYPath += 20;
        mainAnchorPane.getChildren().add(text);
    }

    @Override
    public void start(Stage stage) throws Exception {

    }
}

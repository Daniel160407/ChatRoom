package com.example.chatroom.controllers;

import com.example.chatroom.dataClasses.InputtedData;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class HomeController {
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private TextField textField;
    protected static InputtedData inputtedData = new InputtedData();
    private List<StackPane> sentMessages = new ArrayList<>();
    private int previousSentMessageYPath = 630;
    private int previousReceivedMessageYPath = 630;

    @FXML
    private void onTextFieldAction() {
        inputtedData.setMessage(textField.getText());
        StackPane stackPane = new StackPane();
        stackPane.setLayoutX(270);
        stackPane.setLayoutY(previousSentMessageYPath);
        Rectangle rec = new Rectangle(200, 30, Color.web("#607d8b"));
        rec.setArcWidth(20);
        rec.setArcHeight(20);
        Text text = new Text();
        text.setWrappingWidth(190);
        text.setStyle("-fx-font: 20px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
        text.setText(textField.getText());
        stackPane.getChildren().addAll(rec, text);
        sentMessages.add(stackPane);
        mainAnchorPane.getChildren().removeIf(node -> node instanceof StackPane);
        for (int i = sentMessages.size() - 1; i >= 0; i--) {
            sentMessages.get(i).setLayoutY(previousSentMessageYPath);
            previousSentMessageYPath -= 40;
        }
        mainAnchorPane.getChildren().addAll(sentMessages);
        previousSentMessageYPath = 630;
        textField.clear();
    }

    public void receivedMessageDisplay(String message) {
        StackPane stackPane = new StackPane();
        stackPane.setLayoutX(23);
        stackPane.setLayoutY(previousReceivedMessageYPath);
        Rectangle rec = new Rectangle(200, 30, Color.web("#374151"));
        rec.setArcWidth(20);
        rec.setArcHeight(20);
        Text text = new Text();
        text.setWrappingWidth(190);
        text.setStyle("-fx-font: 20px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
        text.setText(message);
        stackPane.getChildren().addAll(rec, text);
        sentMessages.add(stackPane);
        mainAnchorPane.getChildren().removeIf(node -> node instanceof StackPane);
        for (int i = sentMessages.size() - 1; i >= 0; i--) {
            sentMessages.get(i).setLayoutY(previousReceivedMessageYPath);
            previousReceivedMessageYPath -= 40;
        }
        mainAnchorPane.getChildren().addAll(sentMessages);
        previousReceivedMessageYPath = 630;
        textField.clear();
    }
}
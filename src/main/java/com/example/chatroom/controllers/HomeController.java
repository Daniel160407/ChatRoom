package com.example.chatroom.controllers;

import com.example.chatroom.dataClasses.InputtedData;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeController {
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private TextField textField;
    public static InputtedData inputtedData = new InputtedData();
    private final List<VBox> sentMessages = new ArrayList<>();
    private final List<Text> usernames = new ArrayList<>();
    private int previousSentMessageYPath = 630;
    private int previousReceivedMessageYPath = 630;

    @FXML
    private void onTextFieldAction() {
        inputtedData.setMessage(textField.getText());
        StackPane stackPane = new StackPane();
        Rectangle rec = new Rectangle(200, 30, Color.web("#607d8b"));
        rec.setArcWidth(20);
        rec.setArcHeight(20);
        Text text = new Text();
        text.setWrappingWidth(190);
        text.setStyle("-fx-font: 20px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
        text.setText(textField.getText());
        VBox vBox = new VBox();
        vBox.setLayoutX(270);
        vBox.setLayoutY(previousReceivedMessageYPath);
        stackPane.getChildren().addAll(rec, text);
        vBox.getChildren().add(stackPane);
        sentMessages.add(vBox);
        mainAnchorPane.getChildren().removeIf(node -> node instanceof VBox);
        for (int i = sentMessages.size() - 1; i >= 0; i--) {
            sentMessages.get(i).setLayoutY(previousSentMessageYPath);
            previousSentMessageYPath -= 45;
        }
        mainAnchorPane.getChildren().addAll(sentMessages);
        previousSentMessageYPath = 630;
        textField.clear();
    }

    public void receivedMessageDisplay(String message) {
        String cutMessage = null;
        String cutUsername = null;
        String patternString = "\\b\\w+:\\s*(.*)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            cutMessage = matcher.group(1);
        }
        pattern = Pattern.compile("\\b(\\w+):");
        matcher = pattern.matcher(message);
        while (matcher.find()) {
            cutUsername = matcher.group(1);
        }
        StackPane stackPane = new StackPane();
        Rectangle rec = new Rectangle(200, 30, Color.web("#374151"));
        rec.setArcWidth(20);
        rec.setArcHeight(20);
        Text text = new Text();
        text.setWrappingWidth(190);
        text.setStyle("-fx-font: 20px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
        text.setText(cutMessage);
        Text username = new Text();
        username.setLayoutX(23);
        username.setStyle("-fx-font: 10px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
        username.setWrappingWidth(50);
        username.setText(cutUsername);
        VBox vBox = new VBox();
        vBox.setLayoutX(23);
        vBox.setLayoutY(previousReceivedMessageYPath);
        stackPane.getChildren().addAll(rec, text);
        vBox.getChildren().addAll(username, stackPane);
        sentMessages.add(vBox);
        mainAnchorPane.getChildren().removeIf(node -> node instanceof VBox);
        for (int i = sentMessages.size() - 1; i >= 0; i--) {
            sentMessages.get(i).setLayoutY(previousReceivedMessageYPath);
            previousReceivedMessageYPath -= 45;
        }
        mainAnchorPane.getChildren().addAll(sentMessages);
        previousReceivedMessageYPath = 630;
        textField.clear();
    }

    public InputtedData getInputtedData() {
        return inputtedData;
    }
}
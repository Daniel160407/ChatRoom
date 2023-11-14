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
    private int previousMessageYPath = 650;

    @FXML
    private void onTextFieldAction() {
        inputtedData.setMessage(textField.getText());
        StackPane stackPane = new StackPane();
        stackPane.setLayoutX(350);
        stackPane.setLayoutY(previousMessageYPath);
        Rectangle rec = new Rectangle(200, 30, Color.web("#374151"));
        rec.setArcWidth(20);
        rec.setArcHeight(20);
        Text text = new Text();
        text.setWrappingWidth(190);
        text.setStyle("-fx-font: 20px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
        text.setText(textField.getText());
        stackPane.getChildren().addAll(rec, text);
        sentMessages.add(stackPane);
        mainAnchorPane.getChildren().removeIf(node -> node instanceof StackPane);
        System.out.println(sentMessages.get(0).getChildren().get(1));
        for (int i = 0; i < sentMessages.size(); i++) {
            mainAnchorPane.getChildren().add(sentMessages.get(i));
        }
        previousMessageYPath -= 30;
        textField.clear();
    }
}
package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.dataClasses.InputtedData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeController {
    @FXML
    private AnchorPane mainAnchorPane;
    @FXML
    private TextField textField;
    @FXML
    public ComboBox<String> onlineMembers;
    @FXML
    private Pane profilePane;
    @FXML
    private Pane changeUsernamePane;
    @FXML
    private TextField changeUsernameUsernameField;
    @FXML
    public Label onlineStatus;
    @FXML
    private Label userConnectDisconnectNotification;

    public static InputtedData inputtedData = new InputtedData();
    private final List<VBox> sentMessages = new ArrayList<>();
    public ConnectController connectController;
    private int previousSentMessageYPath = 600;
    private int previousReceivedMessageYPath = 600;

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
        previousSentMessageYPath = 600;
        textField.clear();
        onlineMembers.setValue(null);
    }

    @FXML
    private void onTextFieldKeyTyped() {
        char[] textFieldTextAsArray = textField.getText().toCharArray();
        if (textFieldTextAsArray.length == 1 && textFieldTextAsArray[0] == '@') {
            connectController.client.sendSpecificMessage("#getAllOnlineMembers#");
            textField.clear();
        }
    }

    @FXML
    private void onOnlineMembersAction() {
        inputtedData.setPrivateMessageReceiverUsername(onlineMembers.getValue());
        onlineMembers.setVisible(false);
    }

    @FXML
    private void onAvatarMouseClicked() {
        profilePane.setVisible(!profilePane.isVisible());
        System.out.println(connectController);
    }

    @FXML
    private void onChangeUsernameMouseClicked() {
        changeUsernamePane.setVisible(!changeUsernamePane.isVisible());
    }

    @FXML
    private void onChangeUsernameUsernameFieldAction() {
        getInputtedData().setNewUsername(changeUsernameUsernameField.getText());
        changeUsernameRequest();
        changeUsernamePane.setVisible(false);
        profilePane.setVisible(false);
        connectController.client.sendSpecificMessage("#addUsername#: " + getInputtedData().getUsername());
    }

    @FXML
    private void onChangeUsernameSaveButtonAction() {
        getInputtedData().setNewUsername(changeUsernameUsernameField.getText());
        changeUsernameRequest();
        changeUsernamePane.setVisible(false);
        profilePane.setVisible(false);
        connectController.client.sendSpecificMessage("#addUsername#: " + getInputtedData().getUsername());
    }

    @FXML
    private void onDisconnectButtonAction() throws IOException {
        connectController.exitRequest();
        connectController.client.getSocket().close();
        textField.getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/connect.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        ConnectController connectController = fxmlLoader.getController();
        connectController.connectController = fxmlLoader.getController();
        Stage stage = new Stage();
        stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
        stage.setTitle("ChatRoom");
        stage.setScene(scene);
        stage.show();
    }

    private void changeUsernameRequest() {
        inputtedData.setUsername(changeUsernameUsernameField.getText());
    }

    public void receivedMessageDisplay(String message) {
        // \(([^)]*)\)
        System.out.println("Message: " + message);
        String cutMessage = null;
        String cutUsername = null;
        String patternString = ":\\s*(\\w+)";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            cutMessage = matcher.group(1);
        }
        pattern = Pattern.compile("^(\\w+):");
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
        VBox vBox = new VBox();
        vBox.setLayoutX(23);
        vBox.setLayoutY(previousReceivedMessageYPath);
        stackPane.getChildren().addAll(rec, text);
        System.out.println("MESSAGE:");
        System.out.println(message);
        System.out.println(cutMessage);
        assert cutUsername != null;
        if (cutUsername.equals("null")) {
            vBox.getChildren().add(stackPane);
        } else {
            Text username = new Text();
            username.setLayoutX(23);
            username.setStyle("-fx-font: 10px; " + "-fx-font-weight: bold; " + "-fx-fill: white");
            username.setWrappingWidth(50);
            username.setText(cutUsername);
            vBox.getChildren().addAll(username, stackPane);
        }
        sentMessages.add(vBox);
        mainAnchorPane.getChildren().removeIf(node -> node instanceof VBox);
        for (int i = sentMessages.size() - 1; i >= 0; i--) {
            sentMessages.get(i).setLayoutY(previousReceivedMessageYPath);
            previousReceivedMessageYPath -= 45;
        }
        mainAnchorPane.getChildren().addAll(sentMessages);
        previousReceivedMessageYPath = 600;
        textField.clear();
    }

    public void userConnectDisconnectDisplay(String notification) {
        new Thread(() -> {
            Platform.runLater(() -> {
                userConnectDisconnectNotification.setText(notification);
                userConnectDisconnectNotification.setVisible(true);
            });
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Platform.runLater(() -> userConnectDisconnectNotification.setVisible(false));
        }).start();
    }

    public InputtedData getInputtedData() {
        return inputtedData;
    }

}
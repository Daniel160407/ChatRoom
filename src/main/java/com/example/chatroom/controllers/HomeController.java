package com.example.chatroom.controllers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.dataClasses.InputtedData;
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
import java.util.Stack;
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
    private PasswordField changeUsernamePasswordField;
    @FXML
    private TextField changeUsernameUsernameField;
    @FXML
    private Label changeUsernameErrorMessage;

    public static InputtedData inputtedData = new InputtedData();
    private final List<VBox> sentMessages = new ArrayList<>();
    public LogInController logInController;
    public boolean onlineMembersRequest;
    private int previousSentMessageYPath = 600;
    private int previousReceivedMessageYPath = 600;

    @FXML
    private void onTextFieldAction() {
        inputtedData.setMessage(textField.getText());
        if (onlineMembers.getValue() != null) {
            logInController.client.sendSpecificMessage("#privateMessage#: " + inputtedData.getPrivateMessageReceiverUsername());
            System.out.println(inputtedData.getPrivateMessageReceiverUsername());
        }
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
    }

    @FXML
    private void onTextFieldKeyTyped() {
        char[] textFieldTextAsArray = textField.getText().toCharArray();
        if (textFieldTextAsArray.length == 1 && textFieldTextAsArray[0] == '@') {
            logInController.client.sendSpecificMessage("#getAllOnlineMembers#");
            System.out.println("Opa");
        } else {
            onlineMembersRequest = false;
        }
    }

    @FXML
    private void onOnlineMembersAction() {
        inputtedData.setPrivateMessageReceiverUsername(onlineMembers.getValue());
    }

    @FXML
    private void onAvatarMouseClicked() {
        profilePane.setVisible(true);
    }

    @FXML
    private void onChangeUsernameMouseClicked() {
        changeUsernamePane.setVisible(true);
    }

    @FXML
    private void onChangeUsernamePasswordFieldAction() {
        System.out.println(changeUsernamePasswordField.getText());
        getInputtedData().setPasswordToBeChecked(changeUsernamePasswordField.getText());
    }

    @FXML
    private void onChangeUsernameUsernameFieldAction() {
        getInputtedData().setNewUsername(changeUsernameUsernameField.getText());
        changeUsernameRequest();
    }

    @FXML
    private void onChangeUsernameSaveButtonAction() {
        changeUsernameRequest();
    }

    @FXML
    private void onDisconnectButtonAction() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/logIn.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 497, 733);
        Stage stage = new Stage();
        stage.getIcons().add(new Image("https://cdn.pixabay.com/photo/2021/03/02/12/03/messenger-6062243_1280.png"));
        stage.setTitle("ChatRoom");
        stage.setScene(scene);
        stage.show();
        mainAnchorPane.getScene().getWindow().hide();
        logInController.clientInputOutputProvider.exitRequest();
    }

    private void changeUsernameRequest() {
        boolean hasChanged = false;
        for (int i = 0; i < logInController.registeredPeople.data.size(); i++) {
            System.out.println("Actual password: " + logInController.registeredPeople.data.get(i).get(1));
            System.out.println("Password provided: " + inputtedData.getPasswordToBeChecked());
            if (logInController.registeredPeople.data.get(i).get(1).equals(inputtedData.getPasswordToBeChecked())) {
                getInputtedData().setUsername(changeUsernameUsernameField.getText());
                System.out.println("Old username: " + logInController.registeredPeople.data.get(i).get(2));
                logInController.registeredPeople.data.get(i).remove(2);
                logInController.registeredPeople.data.get(i).add(inputtedData.getNewUsername());
                System.out.println("New username: " + logInController.registeredPeople.data.get(i).get(2));
                hasChanged = true;
                changeUsernamePane.setVisible(false);
                profilePane.setVisible(false);
                break;
            }
        }
        if (!hasChanged) {
            changeUsernameErrorMessage.setText("You have written wrong password!");
        }
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
        pattern = Pattern.compile("(\\w+)\\s*\\:");
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
        previousReceivedMessageYPath = 600;
        textField.clear();
    }

    public InputtedData getInputtedData() {
        return inputtedData;
    }
}
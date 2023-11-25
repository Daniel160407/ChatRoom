package com.example.chatroom.client;

import com.example.chatroom.controllers.HomeController;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Client extends HomeController {
    private String username;
    private final Socket socket;
    private final DataInputStream dataInputStream;
    protected DataOutputStream dataOutputStream;
    public final HomeController homeController;
    private final Thread send = new Thread(this::send);
    private final Thread receive = new Thread(this::receive);

    public Client(String address, int port, HomeController homeController) throws IOException {
        socket = new Socket(address, port);
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        send.start();
        receive.start();
        this.homeController = homeController;
    }


    public void send() {
        try {
            String message;
            while (true) {
                while (true) {
                    System.out.print("");
                    if (inputtedData.getMessage() != null) {
                        break;
                    }
                }
                if (homeController.onlineMembers.getValue() != null) {
                    message = homeController.onlineMembers.getValue() + ": #encryptedMessage#: #privateMessage#: " + inputtedData.getMessage();
                } else {
                    message = inputtedData.getMessage();
                }
                System.out.println(message);
                inputtedData.setMessage(null);
                dataOutputStream.writeUTF(homeController.getInputtedData().getUsername() + ": " + message);
                dataOutputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receive() {
        try {
            while (true) {
                String receivedMessage;
                try {
                    receivedMessage = dataInputStream.readUTF();
                    System.out.println("Client received message: " + receivedMessage);
                    if (receivedMessage.startsWith("#encryptedMessage#: #clientConnected#")) {
                        Platform.runLater(() -> homeController.userConnectDisconnectDisplay("User connected"));
                    } else if (receivedMessage.startsWith("#encryptedMessage#: #clientDisconnected#:")) {
                        Pattern pattern = Pattern.compile("\\s*(\\w+)$");
                        Matcher matcher = pattern.matcher(receivedMessage);
                        if (matcher.find()) {
                            Platform.runLater(() -> homeController.userConnectDisconnectDisplay(matcher.group(1) + " disconnected"));
                        }
                        System.out.println("Error1");
                    } else if (receivedMessage.startsWith("#encryptedMessage#: #countOfOnlineMembers#:")) {
                        Pattern pattern = Pattern.compile(":\\s*(\\d+)$");
                        Matcher matcher = pattern.matcher(receivedMessage);
                        if (matcher.find()) {
                            System.out.println("Count: " + matcher.group(1));
                            Platform.runLater(() -> homeController.onlineStatus.setText(matcher.group(1)));
                        }
                        System.out.println("Error2");
                    } else if (receivedMessage.startsWith("#encryptedMessage#:")) {
                        Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
                        Matcher matcher = pattern.matcher(receivedMessage);
                        if (matcher.find()) {
                            Platform.runLater(() -> {
                                homeController.onlineMembers.getItems().clear();
                                homeController.onlineMembers.getItems().addAll(matcher.group(1).split(","));
                                homeController.onlineMembers.setVisible(true);
                            });
                        }
                        System.out.println("Error3");
                        continue;
                    } else {
                        Platform.runLater(() -> homeController.receivedMessageDisplay(receivedMessage));
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/com/example/chatroom/sounds/notification_sound.wav").getAbsoluteFile());
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    }
                } catch (Exception e) {
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSpecificMessage(String message) {
        try {
            System.out.println("sendSpecificMessage: " + message);
            dataOutputStream.writeUTF("#encryptedMessage#: " + message);
            dataOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setUsername(String username) {
        this.username = username;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUsername() {
        return username;
    }
}

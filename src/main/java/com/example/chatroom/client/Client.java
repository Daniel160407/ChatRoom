package com.example.chatroom.client;

import com.example.chatroom.controllers.HomeController;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Client extends HomeController {
    private Socket socket = null;
    private DataInputStream dataInputStream = null;
    protected DataOutputStream dataOutputStream;
    public HomeController homeController = null;
    public boolean threadKiller = false;
    public Thread send = new Thread(this::send);
    public Thread receive = new Thread(this::receive);

    public Client(String address, int port, HomeController homeController) throws IOException {
        try {
            socket = new Socket(address, port);
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
            send.start();
            receive.start();
            this.homeController = homeController;
        } catch (ConnectException e) {
            homeController.connectController.notificationLabel.setText("Server with port you provided, doesn`t exist!");
        }
    }


    public void send() {
        try {
            String message;
            while (!threadKiller) {
                while (!threadKiller) {
                    System.out.print("");
                    if (inputtedData.getMessage() != null) {
                        break;
                    }
                }
                if (threadKiller) {
                    break;
                }
                if (homeController.onlineMembers.getValue() != null) {
                    message = homeController.onlineMembers.getValue() + ": #encryptedMessage#: #privateMessage#: " + inputtedData.getMessage();
                } else {
                    message = inputtedData.getMessage();
                }
                inputtedData.setMessage(null);
                try {
                    dataOutputStream.writeUTF(homeController.getInputtedData().getUsername() + ": " + message);
                    dataOutputStream.flush();
                } catch (SocketException e) {
                    this.threadKiller = true;
                    Platform.runLater(() -> {
                        try {
                            homeController.clientDisconnectAction();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        threadKiller = false;
    }

    public void receive() {
        try {
            while (!threadKiller) {
                String receivedMessage;
                try {
                    receivedMessage = dataInputStream.readUTF();
                    if (receivedMessage.startsWith("#encryptedMessage#: #clientConnected#:")) {
                        Pattern pattern = Pattern.compile(":\\s*([^:]+)$");
                        Matcher matcher = pattern.matcher(receivedMessage);
                        if (matcher.find()) {
                            Platform.runLater(() -> homeController.userConnectDisconnectDisplay(matcher.group(1)));
                        } else {
                            Platform.runLater(() -> homeController.userConnectDisconnectDisplay("User connected"));
                        }

                    } else if (receivedMessage.startsWith("#encryptedMessage#: #clientDisconnected#:")) {
                        Pattern pattern = Pattern.compile("\\s*(\\w+)$");
                        Matcher matcher = pattern.matcher(receivedMessage);
                        if (matcher.find()) {
                            Platform.runLater(() -> homeController.userConnectDisconnectDisplay(matcher.group(1) + " disconnected"));
                        }
                    } else if (receivedMessage.startsWith("#encryptedMessage#: #countOfOnlineMembers#:")) {
                        Pattern pattern = Pattern.compile(":\\s*(\\d+)$");
                        Matcher matcher = pattern.matcher(receivedMessage);
                        if (matcher.find()) {
                            Platform.runLater(() -> homeController.onlineStatus.setText(matcher.group(1)));
                        }
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
            dataOutputStream.writeUTF("#encryptedMessage#: " + message);
            dataOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Socket getSocket() {
        return socket;
    }

}
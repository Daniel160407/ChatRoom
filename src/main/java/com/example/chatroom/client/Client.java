package com.example.chatroom.client;

import com.example.chatroom.controllers.HomeController;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Client extends HomeController {
    private static final String address = "localhost";
    private static final int port = 8080;
    private final Socket socket = new Socket(address, port);
    private final DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
    protected final DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
    public final HomeController homeController;

    public Client(HomeController homeController) throws IOException {
        Thread send = new Thread(this::send);
        Thread receive = new Thread(this::receive);
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
                message = inputtedData.getMessage();
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
                    if (receivedMessage.startsWith("#encryptedMessage#:")) {
                        Pattern pattern = Pattern.compile("\\[([^\\]]+)\\]");
                        Matcher matcher = pattern.matcher(receivedMessage);
                        if (matcher.find()) {
                            homeController.onlineMembers.getItems().clear();
                            homeController.onlineMembers.getItems().addAll(matcher.group(1).split(","));
                            homeController.onlineMembers.setVisible(true);
                            System.out.println("second: " + matcher.group(1));
                        }
                        continue;
                    } else {
                        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/com/example/chatroom/sounds/notification_sound.wav").getAbsoluteFile());
                        Clip clip = AudioSystem.getClip();
                        clip.open(audioInputStream);
                        clip.start();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Platform.runLater(() -> homeController.receivedMessageDisplay(receivedMessage));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSpecificMessage(String message) {
        try {
            System.out.println(message);
            dataOutputStream.writeUTF("#encryptedMessage#: " + message);
            dataOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getAllOnlineMembers() {

    }
}

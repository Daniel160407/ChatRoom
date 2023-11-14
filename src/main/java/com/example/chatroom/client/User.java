package com.example.chatroom.client;

import com.example.chatroom.controllers.HomeController;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class User extends HomeController {
    private static String address = "localhost";
    private static int port = 8080;
    private String receivedMessage;
    private final Socket socket = new Socket(address, port);
    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

    public User() throws IOException {
        Thread send = new Thread(this::send);
        Thread receive = new Thread(this::receive);
        send.start();
        receive.start();
    }

    public void send() {
        try {
            String message;
            while (true) {
                while (true) {
                    System.out.print("");
                    if (inputtedData.getMessage() != null) {
                        System.out.println("Error2");
                        break;
                    }
                }
                message = inputtedData.getMessage();
                inputtedData.setMessage(null);
                dataOutputStream.writeUTF(message);
                dataOutputStream.flush();
                System.out.println("Sent");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void receive() {
        try {

            while (true) {
                try {
                    receivedMessage = dataInputStream.readUTF();
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("src/main/resources/com/example/chatroom/sounds/notification_sound.wav").getAbsoluteFile());
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                System.out.println("< " + receivedMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

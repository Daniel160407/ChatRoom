package com.example.chatroom.servers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.controllers.ServerController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRoomServer extends ServerController {
    private final List<Socket> outputStreams = new ArrayList<>();
    private final List<String> onlineMembersUsernames = new ArrayList<>();
    private ServerController serverController;
    private final Map<String, Socket> onlineMembers = new HashMap<>();
    private int countOfOnlineMembers = 0;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/server.fxml"));
        Parent root = loader.load();
        serverController = loader.getController();
        stage.setTitle("Server");
        stage.getIcons().add(new Image("https://static-00.iconduck.com/assets.00/server-icon-512x512-06jrsmox.png"));
        Scene scene = new Scene(root, 800, 600);
        stage.setScene(scene);
        stage.show();
        Thread serverThread = new Thread(() -> {
            while (true) {
                System.out.print("");
                if (serverController.inputtedData.getPort() != 0) {
                    break;
                }
            }
            try (ServerSocket serverSocket = new ServerSocket(serverController.inputtedData.getPort())) {
                Platform.runLater(() -> serverController.addNotificationText("Server is running and waiting for connections..."));
                while (true) {
                    Socket socket = serverSocket.accept();
                    Platform.runLater(() -> serverController.addNotificationText("Client connected: " + socket));
                    outputStreams.add(socket);

                    // Create a new thread to handle the client
                    new Thread(() -> handleClient(socket)).start();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        serverThread.start();
    }

    public static void main(String[] args) {
        launch();
    }

    private void handleClient(Socket socket) {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            // Read the username once when the client connects
            countOfOnlineMembers++;
            sendMessageToAllExceptSender("#encryptedMessage#: #countOfOnlineMembers#: " + countOfOnlineMembers, socket);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("#encryptedMessage#: #countOfOnlineMembers#: " + countOfOnlineMembers);
            dataOutputStream.flush();
            dataOutputStream.writeUTF("#encryptedMessage#: #clientConnected#: Welcome to ChatRoom!");
            sendMessageToAllExceptSender("#encryptedMessage#: #clientConnected#:", socket);
            while (true) {
                String message = dataInputStream.readUTF();
                System.out.println(message);
                if (message.contains("#encryptedMessage#: #privateMessage#:")) {
                    Pattern pattern = Pattern.compile(":\\s*(\\w+):");
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        dataOutputStream = new DataOutputStream(onlineMembers.get(matcher.group(1)).getOutputStream());
                        dataOutputStream.writeUTF(message);
                        dataOutputStream.flush();
                    }
                    System.out.println("Private message");
                    System.out.println(onlineMembers.get(matcher.group(1)));
                } else if (message.endsWith("#getAllOnlineMembers#")) {
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    dataOutputStream.writeUTF("#encryptedMessage#: #getAllOnlineMembers#: " + onlineMembersUsernames);
                    System.out.println("Server: " + onlineMembersUsernames);
                } else if (message.contains("#encryptedMessage#: #deleteMember#:")) {
                    Pattern pattern = Pattern.compile(":\\s*(\\w+)$");
                    Matcher matcher = pattern.matcher(message);
                    System.out.println("Username to be deleted: " + message);
                    if (matcher.find()) {
                        dataOutputStream = new DataOutputStream(socket.getOutputStream());
                        dataOutputStream.writeUTF("#encryptedMessage#: #deleteMember#: " + matcher.group(1));
                        sendMessageToAllExceptSender("#encryptedMessage#: #disconnection#: " + onlineMembers.get(matcher.group(1)), socket);
                        System.out.println(matcher.group(1));
                    }

                } else if (message.matches(".*:\\s*(\\w+).*") && message.startsWith("#encryptedMessage#: #addUsername#:")) {
                    Pattern pattern = Pattern.compile("(.*:\\s*(\\w+).*)");
                    Matcher matcher = pattern.matcher(message);
                    if (matcher.find()) {
                        System.out.println("Matcher found:");
                        System.out.println(matcher.group(1));
                        System.out.println(matcher.group(2));
                        onlineMembers.put(matcher.group(2), socket);
                        onlineMembersUsernames.add(matcher.group(2));
                    }
                    System.out.println("2");
                } else {
                    // Send the message to all clients except the sender
                    System.out.println(message);
                    sendMessageToAllExceptSender(message, socket);
                    System.out.println("3");
                }
            }
        } catch (IOException e) {
            // Handle client disconnect
            Platform.runLater(() -> serverController.addNotificationText("Client disconnected: " + socket));
            countOfOnlineMembers--;
            try {
                sendMessageToAllExceptSender("#encryptedMessage#: #countOfOnlineMembers#: " + countOfOnlineMembers, socket);
                boolean sent = false;
                for (String onlineMembersUsername : onlineMembersUsernames) {
                    System.out.println(onlineMembersUsername);
                    if (onlineMembers.get(onlineMembersUsername) == socket) {
                        sendMessageToAllExceptSender("#encryptedMessage#: #clientDisconnected#: " + onlineMembersUsername, socket);
                        onlineMembersUsernames.remove(onlineMembersUsername);
                        onlineMembers.remove(onlineMembersUsername);
                        sent = true;
                        break;
                    }
                }
                if (!sent) {
                    sendMessageToAllExceptSender("#encryptedMessage#: #clientDisconnected#: User", socket);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            outputStreams.removeIf(outputStream -> outputStream.equals(socket));
        }
    }

    private void sendMessageToAllExceptSender(String message, Socket senderSocket) throws IOException {
        for (int i = 0; i < outputStreams.size(); i++) {
            if (!outputStreams.get(i).equals(senderSocket)) {
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStreams.get(i).getOutputStream());
                    dataOutputStream.writeUTF(message);
                } catch (IOException e) {
                    // Handle the exception (e.g., remove the disconnected client)
                    e.printStackTrace();
                }
            }
        }
    }
}

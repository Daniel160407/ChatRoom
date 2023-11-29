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
        stage.setOnCloseRequest(event -> System.exit(0));
        Thread serverThread = new Thread(() -> {
            do {
                System.out.print("");
            } while (serverController.inputtedData.getPort() == 0);
            try (ServerSocket serverSocket = new ServerSocket(serverController.inputtedData.getPort())) {
                Platform.runLater(() -> serverController.addNotificationText("Server is running and waiting for connections..."));
                while (true) {
                    Socket socket = serverSocket.accept();
                    Platform.runLater(() -> serverController.addNotificationText("Client connected: " + socket));
                    outputStreams.add(socket);
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
            countOfOnlineMembers++;
            sendMessageToAllExceptSender("#encryptedMessage#: #countOfOnlineMembers#: " + countOfOnlineMembers, socket);
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("#encryptedMessage#: #countOfOnlineMembers#: " + countOfOnlineMembers);
            dataOutputStream.flush();
            dataOutputStream.writeUTF("#encryptedMessage#: #clientConnected#: Welcome to ChatRoom!");
            sendMessageToAllExceptSender("#encryptedMessage#: #clientConnected#:", socket);
            while (true) {
                String message = dataInputStream.readUTF();
                decipherTheMessage(message, socket);
            }
        } catch (IOException e) {
            Platform.runLater(() -> serverController.addNotificationText("Client disconnected: " + socket));
            countOfOnlineMembers--;
            try {
                sendMessageToAllExceptSender("#encryptedMessage#: #countOfOnlineMembers#: " + countOfOnlineMembers, socket);
                boolean sent = false;
                for (String onlineMembersUsername : onlineMembersUsernames) {
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

    private void decipherTheMessage(String message, Socket socket) throws IOException {
        DataOutputStream dataOutputStream;
        if (message.contains("#encryptedMessage#: #privateMessage#:")) {
            Pattern pattern = Pattern.compile("(\\w+):");
            Matcher matcher = pattern.matcher(message);
            String sender = null;
            String receiver = null;
            String sentMessage = null;
            if (matcher.find()) {
                sender = matcher.group(1);
                if (matcher.find()) {
                    receiver = matcher.group(1);
                }
            }
            pattern = Pattern.compile("#privateMessage#: (.+)");
            matcher = pattern.matcher(message);
            if (matcher.find()) {
                sentMessage = matcher.group(1);
            }
            dataOutputStream = new DataOutputStream(onlineMembers.get(receiver).getOutputStream());
            dataOutputStream.writeUTF(sender + ": " + sentMessage);
            dataOutputStream.flush();
        } else if (message.endsWith("#getAllOnlineMembers#")) {
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeUTF("#encryptedMessage#: #getAllOnlineMembers#: " + onlineMembersUsernames);
        } else if (message.contains("#encryptedMessage#: #deleteMember#:")) {
            Pattern pattern = Pattern.compile(":\\s*(\\w+)$");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                onlineMembersUsernames.remove(matcher.group(1));
                onlineMembers.remove(matcher.group(1));
            }
        } else if (message.matches(".*:\\s*(\\w+).*") && message.startsWith("#encryptedMessage#: #addUsername#:")) {
            Pattern pattern = Pattern.compile("(.*:\\s*(\\w+).*)");
            Matcher matcher = pattern.matcher(message);
            if (matcher.find()) {
                onlineMembers.put(matcher.group(2), socket);
                onlineMembersUsernames.add(matcher.group(2));
            }
        } else {
            sendMessageToAllExceptSender(message, socket);
        }
    }

    private void sendMessageToAllExceptSender(String message, Socket senderSocket) throws IOException {
        for (Socket outputStream : outputStreams) {
            if (!outputStream.equals(senderSocket)) {
                try {
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream.getOutputStream());
                    dataOutputStream.writeUTF(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

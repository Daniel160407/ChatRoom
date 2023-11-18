package com.example.chatroom.servers;

import com.example.chatroom.ChatRoomMain;
import com.example.chatroom.controllers.ServerController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomServer extends ServerController {
    private static final List<Socket> outputStreams = new ArrayList<>();
    private static ServerController serverController;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(ChatRoomMain.class.getResource("fxml files/server.fxml"));
        Parent root = loader.load();
        serverController = loader.getController();
        stage.setTitle("Server");
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

    private static void handleClient(Socket socket) {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            // Read the username once when the client connects

            while (true) {
                String message = dataInputStream.readUTF();
                System.out.println(message);
                // Send the message to all clients except the sender
                sendMessageToAllExceptSender(message, socket);
            }
        } catch (IOException e) {
            // Handle client disconnect
            Platform.runLater(() -> serverController.addNotificationText("Client disconnected: " + socket));
            outputStreams.removeIf(outputStream -> outputStream.equals(socket));
        }
    }

    private static void sendMessageToAllExceptSender(String message, Socket senderSocket) throws IOException {
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

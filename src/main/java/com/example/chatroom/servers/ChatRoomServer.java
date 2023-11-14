package com.example.chatroom.servers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomServer {
    private static int port = 8080;
    private static final List<Socket> outputStreams = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running and waiting for connections...");
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connected: " + socket);
                DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
                outputStreams.add(socket);

                // Create a new thread to handle the client
                new Thread(() -> handleClient(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleClient(Socket socket) {
        try (DataInputStream dataInputStream = new DataInputStream(socket.getInputStream())) {
            // Read the username once when the client connects

            while (true) {
                String message = dataInputStream.readUTF();
                System.out.println("message: " + message);

                // Send the message to all clients except the sender
                sendMessageToAllExceptSender(message, socket);
            }
        } catch (IOException e) {
            // Handle client disconnect
            System.out.println("Client disconnected: " + socket);
            outputStreams.removeIf(outputStream -> outputStream.equals(socket));
        }
    }

    private static void sendMessageToAllExceptSender(String message, Socket senderSocket) throws IOException {
        System.out.println("Size: " + outputStreams.size());
        System.out.println("Sockets: " + outputStreams);
        System.out.println("Sender: "+senderSocket);
        for (int i = 0; i < outputStreams.size(); i++) {
            if (!outputStreams.get(i).equals(senderSocket)) {
                try {
                    System.out.println("sending");
                    DataOutputStream dataOutputStream=new DataOutputStream(outputStreams.get(i).getOutputStream());
                    dataOutputStream.writeUTF(message);
                    System.out.println("sent");
                } catch (IOException e) {
                    // Handle the exception (e.g., remove the disconnected client)
                    e.printStackTrace();
                }
            }
        }
    }
}

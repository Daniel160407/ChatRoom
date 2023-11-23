package com.example.chatroom.client;

import com.example.chatroom.controllers.HomeController;
import com.example.chatroom.controllers.LogInController;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientInputOutputProvider {
    private final LogInController logInController;
    private final HomeController homeController;

    public ClientInputOutputProvider(LogInController logInController, HomeController homeController) {
        this.logInController = logInController;
        this.homeController = homeController;
    }

    public void exitRequest(){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader("src/main/resources/com/example/chatroom/txt files/personalData.txt"));
            List<List<String>> fileData = new ArrayList<>();
            List<String> data = new ArrayList<>();
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                data.add(str);
            }
            for (int i = 2; i < data.size(); i += 3) {
                List<String> sortedData = new ArrayList<>();
                sortedData.add(data.get(i - 2));
                sortedData.add(data.get(i - 1));
                sortedData.add(data.get(i));
                fileData.add(sortedData);
            }
            for (int i = 0; i < fileData.size(); i++) {
                if (!logInController.registeredPeople.data.contains(fileData.get(i))) {
                    logInController.registeredPeople.data.add(fileData.get(i));
                }
            }
            bufferedReader.close();
            System.out.println("Sizes: "+logInController.registeredPeople.data.size());
            for (int i = 0; i < logInController.registeredPeople.data.size() - 1; i++) {
                for (int j = i + 1; j < logInController.registeredPeople.data.size(); j++) {
                    if (logInController.registeredPeople.data.get(i).get(0).equals(logInController.registeredPeople.data.get(j).get(0))) {
                        System.out.println("Registered people:");
                        System.out.println(logInController.registeredPeople.data.get(i).get(0));
                        System.out.println(logInController.registeredPeople.data.get(j).get(0));
                        logInController.registeredPeople.data.remove(j);
                    }
                }
            }
            FileWriter fileWriter = new FileWriter("src/main/resources/com/example/chatroom/txt files/personalData.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            for (int i = 0; i < logInController.registeredPeople.data.size(); i++) {
                for (int j = 0; j < logInController.registeredPeople.data.get(i).size(); j++) {
                    if (logInController.registeredPeople.data.get(i).get(j) != null) {
                        bufferedWriter.write(logInController.registeredPeople.data.get(i).get(j));
                        bufferedWriter.newLine();
                    }
                }
            }
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        logInController.client.sendSpecificMessage("#deleteMember#: " + homeController.getInputtedData().getUsername());
    }
}

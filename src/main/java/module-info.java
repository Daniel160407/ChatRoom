module com.example.chatroom {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;



    exports com.example.chatroom.controllers;
    exports com.example.chatroom.servers to javafx.graphics;
    exports com.example.chatroom.dataClasses;
    opens com.example.chatroom.controllers to javafx.fxml;
    exports com.example.chatroom.client;
    opens com.example.chatroom.client to javafx.fxml;
    exports com.example.chatroom;
    opens com.example.chatroom to javafx.fxml;
}
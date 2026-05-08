package com.campuslostfound.server;

import com.campuslostfound.client.util.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class ServerApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        SceneNavigator.load(stage, "/com/campuslostfound/fxml/admin_server.fxml", "Campus Lost & Found - Admin Server");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

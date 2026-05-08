package com.campuslostfound.client;

import com.campuslostfound.client.util.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class ClientApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        AppSession.configureNetworkHandlers();
        SceneNavigator.load(stage, "/com/campuslostfound/fxml/login.fxml", "Campus Lost & Found - Client");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

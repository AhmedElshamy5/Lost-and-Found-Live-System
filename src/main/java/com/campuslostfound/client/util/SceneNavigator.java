package com.campuslostfound.client.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public final class SceneNavigator {
    private SceneNavigator() {
    }

    public static void go(ActionEvent event, String fxmlPath, String title) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        load(stage, fxmlPath, title);
    }

    public static void load(Stage stage, String fxmlPath, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene scene = new Scene(root, 940, 640);
        stage.setTitle(title);
        stage.setMinWidth(850);
        stage.setMinHeight(560);
        stage.setScene(scene);
    }
}

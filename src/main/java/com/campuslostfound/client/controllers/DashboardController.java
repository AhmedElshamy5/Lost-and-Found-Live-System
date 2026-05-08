package com.campuslostfound.client.controllers;

import com.campuslostfound.client.AppSession;
import com.campuslostfound.client.util.AlertHelper;
import com.campuslostfound.client.util.SceneNavigator;
import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.Message;
import com.campuslostfound.service.ReportValidator;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class DashboardController {
    @FXML private Label welcomeLabel;
    @FXML private Label connectionStatusLabel;
    @FXML private TextField hostField;
    @FXML private TextField portField;
    @FXML private Button connectButton;
    @FXML private Button disconnectButton;

    @FXML
    private void initialize() {
        if (AppSession.getCurrentUser() != null) {
            welcomeLabel.setText("Welcome, " + AppSession.getCurrentUser().getFullName());
        }
        connectionStatusLabel.textProperty().bind(AppSession.connectionStatusProperty());
        updateButtons();
    }

    @FXML
    private void handleConnect() {
        try {
            if (AppSession.getNetworkService().isConnected()) {
                AlertHelper.showInfo("Already Connected", "You are already connected to the server.");
                return;
            }
            String host = hostField.getText().trim();
            ReportValidator.requireText(host, "Host");
            int port = ReportValidator.parsePort(portField.getText());
            AppSession.connectionStatusProperty().set("Connecting...");

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    AppSession.getNetworkService().connect(host, port);
                    AppSession.getNetworkService().send(Message.hello(AppSession.getCurrentUser()));
                    AppSession.getNetworkService().send(Message.requestReports());
                    return null;
                }
            };
            task.setOnSucceeded(event -> updateButtons());
            task.setOnFailed(event -> {
                AppSession.connectionStatusProperty().set("Connection failed.");
                AlertHelper.showError("Connection Failed", task.getException().getMessage());
                updateButtons();
            });
            Thread thread = new Thread(task, "ClientConnectTask");
            thread.setDaemon(true);
            thread.start();
        } catch (ValidationException exception) {
            AlertHelper.showError("Invalid Connection Details", exception.getMessage());
        }
    }

    @FXML
    private void handleDisconnect() {
        try {
            if (AppSession.getNetworkService().isConnected()) {
                AppSession.getNetworkService().send(new Message(com.campuslostfound.model.MessageType.DISCONNECT));
            }
        } catch (IOException ignored) {
        } finally {
            AppSession.getNetworkService().disconnect();
            AppSession.connectionStatusProperty().set("Offline");
            updateButtons();
        }
    }

    @FXML
    private void handleReportLostFound(ActionEvent event) {
        try {
            SceneNavigator.go(event, "/com/campuslostfound/fxml/report_item.fxml", "Campus Lost & Found - Report Item");
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }

    @FXML
    private void handleBrowse(ActionEvent event) {
        try {
            SceneNavigator.go(event, "/com/campuslostfound/fxml/browse_items.fxml", "Campus Lost & Found - Browse Reports");
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }

    @FXML
    private void handleRefreshReports() {
        try {
            if (!AppSession.getNetworkService().isConnected()) {
                AlertHelper.showError("Not Connected", "Connect to the server before refreshing reports.");
                return;
            }
            AppSession.getNetworkService().send(Message.requestReports());
        } catch (IOException exception) {
            AlertHelper.showError("Network Error", exception.getMessage());
        }
    }

    private void updateButtons() {
        boolean connected = AppSession.getNetworkService().isConnected();
        connectButton.setDisable(connected);
        disconnectButton.setDisable(!connected);
    }
}

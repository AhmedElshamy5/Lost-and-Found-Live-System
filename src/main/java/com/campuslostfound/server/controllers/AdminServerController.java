package com.campuslostfound.server.controllers;

import com.campuslostfound.client.util.AlertHelper;
import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.ItemReport;
import com.campuslostfound.network.LostFoundServer;
import com.campuslostfound.network.ServerListener;
import com.campuslostfound.service.ReportValidator;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.List;

public class AdminServerController {
    @FXML private TextField portField;
    @FXML private Label statusLabel;
    @FXML private Button startButton;
    @FXML private Button stopButton;
    @FXML private TextArea logArea;
    @FXML private ListView<String> clientsListView;
    @FXML private TableView<ItemReport> reportsTable;
    @FXML private TableColumn<ItemReport, Integer> idColumn;
    @FXML private TableColumn<ItemReport, String> typeColumn;
    @FXML private TableColumn<ItemReport, String> nameColumn;
    @FXML private TableColumn<ItemReport, String> categoryColumn;
    @FXML private TableColumn<ItemReport, String> locationColumn;
    @FXML private TableColumn<ItemReport, String> statusColumn;
    @FXML private TableColumn<ItemReport, String> reporterColumn;
    @FXML private TextField announcementField;

    private LostFoundServer server;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReportType().name()));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().name()));
        reporterColumn.setCellValueFactory(new PropertyValueFactory<>("reporterName"));
        stopButton.setDisable(true);
        statusLabel.setText("Stopped");
    }

    @FXML
    private void handleStartServer() {
        try {
            int port = ReportValidator.parsePort(portField.getText());
            server = new LostFoundServer(port, createServerListener());
            server.start();
            startButton.setDisable(true);
            stopButton.setDisable(false);
            statusLabel.setText("Running on port " + port);
        } catch (ValidationException exception) {
            AlertHelper.showError("Invalid Port", exception.getMessage());
        } catch (IOException exception) {
            AlertHelper.showError("Server Error", exception.getMessage());
        }
    }

    @FXML
    private void handleStopServer() {
        if (server != null) {
            server.stop();
        }
        startButton.setDisable(false);
        stopButton.setDisable(true);
        statusLabel.setText("Stopped");
    }

    @FXML
    private void handleBroadcast() {
        try {
            ensureServerRunning();
            server.broadcastAnnouncement(announcementField.getText());
            announcementField.clear();
        } catch (ValidationException exception) {
            AlertHelper.showError("Cannot Broadcast", exception.getMessage());
        }
    }

    @FXML
    private void handleMarkReturned() {
        try {
            ensureServerRunning();
            ItemReport selected = reportsTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                throw new ValidationException("Select a report first.");
            }
            server.markReturned(selected.getId());
        } catch (ValidationException exception) {
            AlertHelper.showError("Cannot Update Report", exception.getMessage());
        }
    }

    @FXML
    private void handleClearLog() {
        logArea.clear();
    }

    private void ensureServerRunning() throws ValidationException {
        if (server == null || !server.isRunning()) {
            throw new ValidationException("Start the server first.");
        }
    }

    private ServerListener createServerListener() {
        return new ServerListener() {
            @Override
            public void onLog(String message) {
                Platform.runLater(() -> logArea.appendText(message + System.lineSeparator()));
            }

            @Override
            public void onClientListChanged(List<String> clients) {
                Platform.runLater(() -> clientsListView.setItems(FXCollections.observableArrayList(clients)));
            }

            @Override
            public void onReportsChanged(List<ItemReport> reports) {
                Platform.runLater(() -> reportsTable.setItems(FXCollections.observableArrayList(reports)));
            }
        };
    }
}

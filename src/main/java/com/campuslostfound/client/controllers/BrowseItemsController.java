package com.campuslostfound.client.controllers;

import com.campuslostfound.client.AppSession;
import com.campuslostfound.client.util.AlertHelper;
import com.campuslostfound.client.util.SceneNavigator;
import com.campuslostfound.model.ItemReport;
import com.campuslostfound.model.Message;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;

public class BrowseItemsController {
    @FXML private TableView<ItemReport> reportsTable;
    @FXML private TableColumn<ItemReport, Integer> idColumn;
    @FXML private TableColumn<ItemReport, String> typeColumn;
    @FXML private TableColumn<ItemReport, String> nameColumn;
    @FXML private TableColumn<ItemReport, String> categoryColumn;
    @FXML private TableColumn<ItemReport, String> locationColumn;
    @FXML private TableColumn<ItemReport, String> statusColumn;
    @FXML private TableColumn<ItemReport, String> reporterColumn;
    @FXML private TableColumn<ItemReport, String> dateColumn;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getReportType().name()));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("itemName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().name()));
        reporterColumn.setCellValueFactory(new PropertyValueFactory<>("reporterName"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("formattedCreatedAt"));
        reportsTable.setItems(AppSession.getReports());
    }

    @FXML
    private void handleRefresh() {
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

    @FXML
    private void handleDetails() {
        ItemReport selected = reportsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            AlertHelper.showError("No Selection", "Select a report first.");
            return;
        }
        AlertHelper.showInfo("Report Details",
                selected.toNetworkLine() + "\n\nCategory: " + selected.getCategory()
                        + "\nLocation: " + selected.getLocation()
                        + "\nStatus: " + selected.getStatus()
                        + "\nReporter: " + selected.getReporterName()
                        + "\nDescription: " + selected.getDescription());
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            SceneNavigator.go(event, "/com/campuslostfound/fxml/dashboard.fxml", "Campus Lost & Found - Dashboard");
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }
}

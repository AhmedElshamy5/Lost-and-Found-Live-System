package com.campuslostfound.client.controllers;

import com.campuslostfound.client.AppSession;
import com.campuslostfound.client.util.AlertHelper;
import com.campuslostfound.client.util.SceneNavigator;
import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.FoundItemReport;
import com.campuslostfound.model.ItemReport;
import com.campuslostfound.model.LostItemReport;
import com.campuslostfound.model.Message;
import com.campuslostfound.service.ReportValidator;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ReportItemController {
    @FXML private ComboBox<String> typeComboBox;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField itemNameField;
    @FXML private TextField locationField;
    @FXML private TextArea descriptionArea;
    @FXML private Label statusLabel;

    @FXML
    private void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList("Lost", "Found"));
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Phone", "Wallet", "ID Card", "Keys", "Laptop", "Book", "Bag", "Other"
        ));
    }

    @FXML
    private void handleSubmit() {
        try {
            if (!AppSession.getNetworkService().isConnected()) {
                throw new ValidationException("Connect to the server before submitting a report.");
            }
            String type = typeComboBox.getValue();
            ReportValidator.requireText(type, "Report type");
            ReportValidator.requireText(categoryComboBox.getValue(), "Category");

            ItemReport report;
            if ("Lost".equals(type)) {
                report = new LostItemReport(
                        0,
                        itemNameField.getText().trim(),
                        categoryComboBox.getValue(),
                        locationField.getText().trim(),
                        descriptionArea.getText().trim(),
                        AppSession.getCurrentUser()
                );
            } else {
                report = new FoundItemReport(
                        0,
                        itemNameField.getText().trim(),
                        categoryComboBox.getValue(),
                        locationField.getText().trim(),
                        descriptionArea.getText().trim(),
                        AppSession.getCurrentUser()
                );
            }

            ReportValidator.validateReport(report);
            AppSession.getNetworkService().send(Message.submitReport(report));
            statusLabel.setText("Report sent to server.");
            clearForm();
        } catch (ValidationException exception) {
            statusLabel.setText(exception.getMessage());
            AlertHelper.showError("Invalid Report", exception.getMessage());
        } catch (IOException exception) {
            statusLabel.setText("Network error.");
            AlertHelper.showError("Network Error", exception.getMessage());
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            SceneNavigator.go(event, "/com/campuslostfound/fxml/dashboard.fxml", "Campus Lost & Found - Dashboard");
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }

    private void clearForm() {
        typeComboBox.getSelectionModel().clearSelection();
        categoryComboBox.getSelectionModel().clearSelection();
        itemNameField.clear();
        locationField.clear();
        descriptionArea.clear();
    }
}

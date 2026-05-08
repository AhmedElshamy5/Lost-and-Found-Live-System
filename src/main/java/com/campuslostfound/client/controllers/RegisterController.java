package com.campuslostfound.client.controllers;

import com.campuslostfound.client.AppSession;
import com.campuslostfound.client.util.AlertHelper;
import com.campuslostfound.client.util.SceneNavigator;
import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class RegisterController {
    @FXML private TextField fullNameField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField contactField;
    @FXML private Label messageLabel;

    @FXML
    private void handleRegister(ActionEvent event) {
        try {
            User user = new User(
                    fullNameField.getText().trim(),
                    usernameField.getText().trim(),
                    passwordField.getText(),
                    contactField.getText().trim()
            );
            AppSession.getUserStore().register(user);
            AlertHelper.showInfo("Registration Complete", "Account created. You can now log in.");
            SceneNavigator.go(event, "/com/campuslostfound/fxml/login.fxml", "Campus Lost & Found - Client");
        } catch (ValidationException exception) {
            messageLabel.setText(exception.getMessage());
            AlertHelper.showError("Registration Failed", exception.getMessage());
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            SceneNavigator.go(event, "/com/campuslostfound/fxml/login.fxml", "Campus Lost & Found - Client");
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }
}

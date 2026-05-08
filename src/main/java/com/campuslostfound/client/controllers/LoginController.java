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

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            User user = AppSession.getUserStore().login(usernameField.getText(), passwordField.getText());
            AppSession.setCurrentUser(user);
            SceneNavigator.go(event, "/com/campuslostfound/fxml/dashboard.fxml", "Campus Lost & Found - Dashboard");
        } catch (ValidationException exception) {
            messageLabel.setText(exception.getMessage());
            AlertHelper.showError("Login Failed", exception.getMessage());
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }

    @FXML
    private void handleGoRegister(ActionEvent event) {
        try {
            SceneNavigator.go(event, "/com/campuslostfound/fxml/register.fxml", "Campus Lost & Found - Register");
        } catch (IOException exception) {
            AlertHelper.showError("Navigation Error", exception.getMessage());
        }
    }
}

package com.campuslostfound.client;

import com.campuslostfound.client.util.AlertHelper;
import com.campuslostfound.model.ItemReport;
import com.campuslostfound.model.Message;
import com.campuslostfound.model.MessageType;
import com.campuslostfound.model.User;
import com.campuslostfound.network.ClientNetworkService;
import com.campuslostfound.service.LocalUserStore;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public final class AppSession {
    private static final LocalUserStore USER_STORE = new LocalUserStore();
    private static final ClientNetworkService NETWORK_SERVICE = new ClientNetworkService();
    private static final ObservableList<ItemReport> REPORTS = FXCollections.observableArrayList();
    private static final StringProperty CONNECTION_STATUS = new SimpleStringProperty("Offline");
    private static User currentUser;
    private static boolean handlersConfigured;

    private AppSession() {
    }

    public static void configureNetworkHandlers() {
        if (handlersConfigured) {
            return;
        }
        NETWORK_SERVICE.setMessageHandler(AppSession::handleServerMessage);
        NETWORK_SERVICE.setStatusHandler(message -> Platform.runLater(() -> CONNECTION_STATUS.set(message)));
        handlersConfigured = true;
    }

    private static void handleServerMessage(Message message) {
        Platform.runLater(() -> {
            if (message.getType() == MessageType.REPORTS_SNAPSHOT) {
                REPORTS.setAll(message.getReports());
            } else if (message.getType() == MessageType.REPORT_ADDED) {
                addOrReplaceReport(message.getReport());
                CONNECTION_STATUS.set("New report received: " + message.getReport().toNetworkLine());
            } else if (message.getType() == MessageType.ADMIN_ANNOUNCEMENT) {
                CONNECTION_STATUS.set(message.getText());
                AlertHelper.showInfo("Admin Announcement", message.getText());
            } else if (message.getType() == MessageType.SERVER_NOTICE || message.getType() == MessageType.SUCCESS) {
                CONNECTION_STATUS.set(message.getText());
            } else if (message.getType() == MessageType.ERROR) {
                CONNECTION_STATUS.set(message.getText());
                AlertHelper.showError("Server Error", message.getText());
            }
        });
    }

    private static void addOrReplaceReport(ItemReport incoming) {
        if (incoming == null) {
            return;
        }
        for (int index = 0; index < REPORTS.size(); index++) {
            if (REPORTS.get(index).getId() == incoming.getId()) {
                REPORTS.set(index, incoming);
                return;
            }
        }
        REPORTS.add(incoming);
    }

    public static LocalUserStore getUserStore() {
        return USER_STORE;
    }

    public static ClientNetworkService getNetworkService() {
        return NETWORK_SERVICE;
    }

    public static ObservableList<ItemReport> getReports() {
        return REPORTS;
    }

    public static StringProperty connectionStatusProperty() {
        return CONNECTION_STATUS;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        AppSession.currentUser = currentUser;
    }
}

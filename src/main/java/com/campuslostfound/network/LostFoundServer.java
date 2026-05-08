package com.campuslostfound.network;

import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.ItemReport;
import com.campuslostfound.model.Message;
import com.campuslostfound.model.MessageType;
import com.campuslostfound.service.ReportManager;
import com.campuslostfound.service.ReportMatcher;
import com.campuslostfound.service.ReportValidator;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LostFoundServer {
    private final int port;
    private final ServerListener listener;
    private final ReportManager reportManager = new ReportManager();
    private final ReportMatcher reportMatcher = new ReportMatcher();
    private final List<ClientHandler> clients = new CopyOnWriteArrayList<>();
    private volatile boolean running;
    private ServerSocket serverSocket;

    public LostFoundServer(int port, ServerListener listener) {
        this.port = port;
        this.listener = listener;
    }

    public void start() throws IOException {
        if (running) {
            return;
        }
        serverSocket = new ServerSocket(port);
        running = true;
        Thread acceptThread = new Thread(this::acceptLoop, "LostFoundServer-AcceptLoop");
        acceptThread.setDaemon(true);
        acceptThread.start();
        log("Server started on port " + port + ".");
    }

    private void acceptLoop() {
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                ClientHandler handler = new ClientHandler(socket, this);
                clients.add(handler);
                handler.start();
                log("New socket connection from " + socket.getRemoteSocketAddress() + ".");
                updateClientList();
            } catch (IOException exception) {
                if (running) {
                    log("Accept loop error: " + exception.getMessage());
                }
            }
        }
    }

    public void handleMessage(ClientHandler handler, Message message) {
        if (message == null || message.getType() == null) {
            handler.send(Message.error("Invalid message received."));
            return;
        }

        try {
            if (message.getType() == MessageType.HELLO) {
                handler.setUser(message.getUser());
                log(handler.getDisplayName() + " joined the server.");
                handler.send(Message.notice("Connected to Campus Lost & Found server."));
                handler.send(Message.reportsSnapshot(reportManager.getReportsSnapshot()));
                updateClientList();
            } else if (message.getType() == MessageType.SUBMIT_REPORT) {
                handleSubmittedReport(handler, message.getReport());
            } else if (message.getType() == MessageType.REQUEST_REPORTS) {
                handler.send(Message.reportsSnapshot(reportManager.getReportsSnapshot()));
            } else if (message.getType() == MessageType.DISCONNECT) {
                handler.close();
            }
        } catch (ValidationException exception) {
            handler.send(Message.error(exception.getMessage()));
            log("Validation error for " + handler.getDisplayName() + ": " + exception.getMessage());
        }
    }

    private void handleSubmittedReport(ClientHandler handler, ItemReport report) throws ValidationException {
        ReportValidator.validateReport(report);
        ItemReport savedReport = reportManager.addReport(report);
        long possibleMatches = reportMatcher.countPossibleMatches(savedReport, reportManager.getReportsSnapshot());

        log(handler.getDisplayName() + " submitted " + savedReport.toNetworkLine() + ".");
        if (possibleMatches > 0) {
            log("Possible match found for report #" + savedReport.getId() + ".");
        }

        handler.send(Message.success("Report submitted successfully."));
        broadcast(Message.reportAdded(savedReport));
        broadcast(Message.reportsSnapshot(reportManager.getReportsSnapshot()));
        updateReports();
    }

    public void broadcastAnnouncement(String text) throws ValidationException {
        ReportValidator.requireText(text, "Announcement");
        Message message = Message.announcement("Admin: " + text.trim());
        broadcast(message);
        log("Admin broadcast: " + text.trim());
    }

    public void markReturned(int reportId) throws ValidationException {
        reportManager.markReturned(reportId);
        broadcast(Message.reportsSnapshot(reportManager.getReportsSnapshot()));
        updateReports();
        log("Report #" + reportId + " marked as returned.");
    }

    public void broadcast(Message message) {
        for (ClientHandler client : clients) {
            client.send(message);
        }
    }

    public void removeClient(ClientHandler handler) {
        clients.remove(handler);
        updateClientList();
    }

    public void stop() {
        running = false;
        for (ClientHandler client : clients) {
            client.send(Message.notice("Server is shutting down."));
            client.close();
        }
        clients.clear();
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException ignored) {
        }
        updateClientList();
        log("Server stopped.");
    }

    public boolean isRunning() {
        return running;
    }

    public List<ItemReport> getReportsSnapshot() {
        return reportManager.getReportsSnapshot();
    }

    public void log(String message) {
        if (listener != null) {
            listener.onLog("[" + LocalTime.now().withNano(0) + "] " + message);
        }
    }

    private void updateClientList() {
        if (listener != null) {
            listener.onClientListChanged(clients.stream().map(ClientHandler::getDisplayName).toList());
        }
    }

    private void updateReports() {
        if (listener != null) {
            listener.onReportsChanged(reportManager.getReportsSnapshot());
        }
    }
}

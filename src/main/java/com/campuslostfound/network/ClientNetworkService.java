package com.campuslostfound.network;

import com.campuslostfound.model.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.function.Consumer;

public class ClientNetworkService {
    private Socket socket;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Thread listenerThread;
    private volatile boolean connected;
    private Consumer<Message> messageHandler = message -> { };
    private Consumer<String> statusHandler = message -> { };

    public void connect(String host, int port) throws IOException {
        if (connected) {
            return;
        }
        socket = new Socket(host, port);
        outputStream = new ObjectOutputStream(socket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(socket.getInputStream());
        connected = true;
        startListening();
        statusHandler.accept("Connected to " + host + ":" + port);
    }

    private void startListening() {
        listenerThread = new Thread(this::listenLoop, "ClientNetworkListener");
        listenerThread.setDaemon(true);
        listenerThread.start();
    }

    private void listenLoop() {
        try {
            while (connected) {
                Object object = inputStream.readObject();
                if (object instanceof Message message) {
                    messageHandler.accept(message);
                }
            }
        } catch (EOFException | SocketException exception) {
            statusHandler.accept("Disconnected from server.");
        } catch (IOException | ClassNotFoundException exception) {
            statusHandler.accept("Network error: " + exception.getMessage());
        } finally {
            disconnect();
        }
    }

    public synchronized void send(Message message) throws IOException {
        if (!connected || outputStream == null) {
            throw new IOException("Not connected to the server.");
        }
        outputStream.writeObject(message);
        outputStream.reset();
        outputStream.flush();
    }

    public void disconnect() {
        connected = false;
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ignored) {
        }
    }

    public boolean isConnected() {
        return connected;
    }

    public void setMessageHandler(Consumer<Message> messageHandler) {
        this.messageHandler = messageHandler == null ? message -> { } : messageHandler;
    }

    public void setStatusHandler(Consumer<String> statusHandler) {
        this.statusHandler = statusHandler == null ? message -> { } : statusHandler;
    }
}

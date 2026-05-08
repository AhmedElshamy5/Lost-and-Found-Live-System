package com.campuslostfound.network;

import com.campuslostfound.model.Message;
import com.campuslostfound.model.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class ClientHandler implements Runnable {
    private final Socket socket;
    private final LostFoundServer server;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private Thread thread;
    private volatile boolean active;
    private User user;

    public ClientHandler(Socket socket, LostFoundServer server) {
        this.socket = socket;
        this.server = server;
    }

    public void start() {
        active = true;
        thread = new Thread(this, "ClientHandler-" + socket.getPort());
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    public void run() {
        try {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());

            while (active) {
                Object object = inputStream.readObject();
                if (object instanceof Message message) {
                    server.handleMessage(this, message);
                }
            }
        } catch (EOFException | SocketException exception) {
            server.log(getDisplayName() + " disconnected.");
        } catch (IOException | ClassNotFoundException exception) {
            server.log("Client connection error: " + exception.getMessage());
        } finally {
            close();
            server.removeClient(this);
        }
    }

    public synchronized void send(Message message) {
        if (outputStream == null || !active) {
            return;
        }
        try {
            outputStream.writeObject(message);
            outputStream.reset();
            outputStream.flush();
        } catch (IOException exception) {
            server.log("Could not send message to " + getDisplayName() + ": " + exception.getMessage());
            close();
        }
    }

    public void close() {
        active = false;
        try {
            socket.close();
        } catch (IOException ignored) {
        }
    }

    public String getDisplayName() {
        if (user != null) {
            return user.getDisplayName();
        }
        return "Client " + socket.getRemoteSocketAddress();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

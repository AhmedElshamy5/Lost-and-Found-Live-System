package com.campuslostfound.service;

import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocalUserStore {
    private final List<User> users = new ArrayList<>();

    public LocalUserStore() {
        users.add(new User("Demo Student", "demo", "demo123", "demo@student.edu"));
    }

    public synchronized void register(User user) throws ValidationException {
        ReportValidator.validateUser(user);
        if (findByUsername(user.getUsername()).isPresent()) {
            throw new ValidationException("This username is already registered.");
        }
        users.add(user);
    }

    public synchronized User login(String username, String password) throws ValidationException {
        ReportValidator.requireText(username, "Username");
        ReportValidator.requireText(password, "Password");

        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username.trim()))
                .filter(user -> user.getPassword().equals(password))
                .findFirst()
                .orElseThrow(() -> new ValidationException("Invalid username or password."));
    }

    public synchronized Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username.trim()))
                .findFirst();
    }
}

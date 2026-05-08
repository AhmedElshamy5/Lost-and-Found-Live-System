package com.campuslostfound.service;

import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.ItemReport;
import com.campuslostfound.model.User;

public final class ReportValidator {
    private ReportValidator() {
    }

    public static void requireText(String value, String fieldName) throws ValidationException {
        if (value == null || value.trim().isEmpty()) {
            throw new ValidationException(fieldName + " is required.");
        }
    }

    public static void validateUser(User user) throws ValidationException {
        if (user == null) {
            throw new ValidationException("User information is missing.");
        }
        requireText(user.getFullName(), "Full name");
        requireText(user.getUsername(), "Username");
        requireText(user.getPassword(), "Password");
        requireText(user.getContact(), "Contact");

        if (user.getUsername().length() < 3) {
            throw new ValidationException("Username must be at least 3 characters.");
        }
        if (user.getPassword().length() < 4) {
            throw new ValidationException("Password must be at least 4 characters.");
        }
    }

    public static void validateReport(ItemReport report) throws ValidationException {
        if (report == null) {
            throw new ValidationException("Report information is missing.");
        }
        requireText(report.getItemName(), "Item name");
        requireText(report.getCategory(), "Category");
        requireText(report.getLocation(), "Location");
        requireText(report.getDescription(), "Description");

        if (report.getReporter() == null) {
            throw new ValidationException("Reporter information is missing.");
        }
    }

    public static int parsePort(String value) throws ValidationException {
        requireText(value, "Port");
        try {
            int port = Integer.parseInt(value.trim());
            if (port < 1024 || port > 65535) {
                throw new ValidationException("Port must be between 1024 and 65535.");
            }
            return port;
        } catch (NumberFormatException exception) {
            throw new ValidationException("Port must be a valid number.");
        }
    }
}

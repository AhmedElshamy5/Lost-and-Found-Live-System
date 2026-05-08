package com.campuslostfound.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private MessageType type;
    private User user;
    private ItemReport report;
    private List<ItemReport> reports;
    private String text;
    private boolean success;

    public Message(MessageType type) {
        this.type = type;
        this.reports = new ArrayList<>();
    }

    public static Message hello(User user) {
        Message message = new Message(MessageType.HELLO);
        message.setUser(user);
        return message;
    }

    public static Message submitReport(ItemReport report) {
        Message message = new Message(MessageType.SUBMIT_REPORT);
        message.setReport(report);
        return message;
    }

    public static Message requestReports() {
        return new Message(MessageType.REQUEST_REPORTS);
    }

    public static Message reportsSnapshot(List<ItemReport> reports) {
        Message message = new Message(MessageType.REPORTS_SNAPSHOT);
        message.setReports(reports);
        return message;
    }

    public static Message reportAdded(ItemReport report) {
        Message message = new Message(MessageType.REPORT_ADDED);
        message.setReport(report);
        return message;
    }

    public static Message notice(String text) {
        Message message = new Message(MessageType.SERVER_NOTICE);
        message.setText(text);
        return message;
    }

    public static Message announcement(String text) {
        Message message = new Message(MessageType.ADMIN_ANNOUNCEMENT);
        message.setText(text);
        return message;
    }

    public static Message error(String text) {
        Message message = new Message(MessageType.ERROR);
        message.setText(text);
        message.setSuccess(false);
        return message;
    }

    public static Message success(String text) {
        Message message = new Message(MessageType.SUCCESS);
        message.setText(text);
        message.setSuccess(true);
        return message;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ItemReport getReport() {
        return report;
    }

    public void setReport(ItemReport report) {
        this.report = report;
    }

    public List<ItemReport> getReports() {
        return reports;
    }

    public void setReports(List<ItemReport> reports) {
        this.reports = reports == null ? new ArrayList<>() : reports;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

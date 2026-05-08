package com.campuslostfound.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class ItemReport implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private int id;
    private String itemName;
    private String category;
    private String location;
    private String description;
    private User reporter;
    private LocalDateTime createdAt;
    private ReportStatus status;

    protected ItemReport(int id, String itemName, String category, String location, String description, User reporter) {
        this.id = id;
        this.itemName = itemName;
        this.category = category;
        this.location = location;
        this.description = description;
        this.reporter = reporter;
        this.createdAt = LocalDateTime.now();
        this.status = ReportStatus.OPEN;
    }

    public abstract ReportType getReportType();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ReportStatus getStatus() {
        return status;
    }

    public void setStatus(ReportStatus status) {
        this.status = status;
    }

    public String getReporterName() {
        return reporter == null ? "Unknown" : reporter.getDisplayName();
    }

    public String getFormattedCreatedAt() {
        return createdAt == null ? "" : FORMATTER.format(createdAt);
    }

    public String toNetworkLine() {
        return "#" + id + " " + getReportType() + " " + itemName + " at " + location;
    }

    @Override
    public String toString() {
        return toNetworkLine();
    }
}

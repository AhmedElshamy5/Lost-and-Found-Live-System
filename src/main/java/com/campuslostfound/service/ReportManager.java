package com.campuslostfound.service;

import com.campuslostfound.exception.ValidationException;
import com.campuslostfound.model.ItemReport;
import com.campuslostfound.model.ReportStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

public class ReportManager {
    private final List<ItemReport> reports = Collections.synchronizedList(new ArrayList<>());
    private final AtomicInteger nextId = new AtomicInteger(1);

    public ItemReport addReport(ItemReport report) throws ValidationException {
        ReportValidator.validateReport(report);
        if (report.getId() <= 0) {
            report.setId(nextId.getAndIncrement());
        }
        reports.add(report);
        return report;
    }

    public List<ItemReport> getReportsSnapshot() {
        synchronized (reports) {
            return new ArrayList<>(reports);
        }
    }

    public Optional<ItemReport> findById(int id) {
        synchronized (reports) {
            return reports.stream().filter(report -> report.getId() == id).findFirst();
        }
    }

    public void markReturned(int reportId) throws ValidationException {
        ItemReport report = findById(reportId)
                .orElseThrow(() -> new ValidationException("Selected report was not found."));
        report.setStatus(ReportStatus.RETURNED);
    }

    public int size() {
        return reports.size();
    }
}

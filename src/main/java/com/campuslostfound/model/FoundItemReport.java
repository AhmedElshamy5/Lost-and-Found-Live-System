package com.campuslostfound.model;

public class FoundItemReport extends ItemReport {
    private static final long serialVersionUID = 1L;

    public FoundItemReport(int id, String itemName, String category, String location, String description, User reporter) {
        super(id, itemName, category, location, description, reporter);
    }

    @Override
    public ReportType getReportType() {
        return ReportType.FOUND;
    }
}

package com.campuslostfound.model;

public class LostItemReport extends ItemReport {
    private static final long serialVersionUID = 1L;

    public LostItemReport(int id, String itemName, String category, String location, String description, User reporter) {
        super(id, itemName, category, location, description, reporter);
    }

    @Override
    public ReportType getReportType() {
        return ReportType.LOST;
    }
}

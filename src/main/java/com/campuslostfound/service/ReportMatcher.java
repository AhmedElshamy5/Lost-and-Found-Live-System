package com.campuslostfound.service;

import com.campuslostfound.model.ItemReport;
import com.campuslostfound.model.ReportType;

import java.util.List;
import java.util.Locale;

public class ReportMatcher {
    public boolean isPossibleMatch(ItemReport first, ItemReport second) {
        if (first == null || second == null) {
            return false;
        }
        if (first.getReportType() == second.getReportType()) {
            return false;
        }
        return sameText(first.getCategory(), second.getCategory())
                && (sameText(first.getLocation(), second.getLocation()) || itemNamesLookSimilar(first, second));
    }

    public long countPossibleMatches(ItemReport candidate, List<ItemReport> reports) {
        return reports.stream()
                .filter(existing -> existing.getId() != candidate.getId())
                .filter(existing -> isPossibleMatch(candidate, existing))
                .count();
    }

    private boolean itemNamesLookSimilar(ItemReport first, ItemReport second) {
        String a = normalize(first.getItemName());
        String b = normalize(second.getItemName());
        return a.contains(b) || b.contains(a);
    }

    private boolean sameText(String first, String second) {
        return normalize(first).equals(normalize(second));
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}

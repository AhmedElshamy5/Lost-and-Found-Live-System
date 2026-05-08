package com.campuslostfound.network;

import com.campuslostfound.model.ItemReport;

import java.util.List;

public interface ServerListener {
    void onLog(String message);
    void onClientListChanged(List<String> clients);
    void onReportsChanged(List<ItemReport> reports);
}

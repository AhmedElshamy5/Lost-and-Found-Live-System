package com.campuslostfound.model;

import java.io.Serializable;

public enum MessageType implements Serializable {
    HELLO,
    SUBMIT_REPORT,
    REQUEST_REPORTS,
    REPORTS_SNAPSHOT,
    REPORT_ADDED,
    ADMIN_ANNOUNCEMENT,
    SERVER_NOTICE,
    ERROR,
    SUCCESS,
    DISCONNECT
}

package com.team5.demo.entities;

public enum SessionStatus {
    SCHEDULED("Scheduled"),
    ONGOING("Ongoing"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    SessionStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}


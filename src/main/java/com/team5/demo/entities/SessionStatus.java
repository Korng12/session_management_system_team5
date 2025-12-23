package com.team5.demo.entities;

public enum SessionStatus {
    DRAFT,          // Session is being prepared
    SCHEDULED,      // Session is scheduled but not started
    IN_PROGRESS,    // Session is currently ongoing
    COMPLETED,      // Session has been completed
    CANCELLED       // Session was cancelled
}

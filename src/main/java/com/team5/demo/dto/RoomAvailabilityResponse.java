package com.team5.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class RoomAvailabilityResponse {
    private boolean available;
    private String message;
    private List<ConflictingSession> conflictingSessions;

    public RoomAvailabilityResponse() {
    }

    public RoomAvailabilityResponse(boolean available, String message) {
        this.available = available;
        this.message = message;
    }

    public RoomAvailabilityResponse(boolean available, String message, List<ConflictingSession> conflictingSessions) {
        this.available = available;
        this.message = message;
        this.conflictingSessions = conflictingSessions;
    }

    // Getters and Setters
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ConflictingSession> getConflictingSessions() {
        return conflictingSessions;
    }

    public void setConflictingSessions(List<ConflictingSession> conflictingSessions) {
        this.conflictingSessions = conflictingSessions;
    }

    // Inner class for conflicting session details
    public static class ConflictingSession {
        private Long id;
        private String title;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public ConflictingSession() {
        }

        public ConflictingSession(Long id, String title, LocalDateTime startTime, LocalDateTime endTime) {
            this.id = id;
            this.title = title;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public LocalDateTime getStartTime() {
            return startTime;
        }

        public void setStartTime(LocalDateTime startTime) {
            this.startTime = startTime;
        }

        public LocalDateTime getEndTime() {
            return endTime;
        }

        public void setEndTime(LocalDateTime endTime) {
            this.endTime = endTime;
        }
    }
}

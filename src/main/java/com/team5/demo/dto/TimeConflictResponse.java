package com.team5.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

public class TimeConflictResponse {
    private boolean hasConflict;
    private String message;
    private List<ConflictingSession> conflictingSessions;
    private String conflictType; // "CHAIR" or "ROOM"

    public TimeConflictResponse() {
    }

    public TimeConflictResponse(boolean hasConflict, String message, String conflictType) {
        this.hasConflict = hasConflict;
        this.message = message;
        this.conflictType = conflictType;
    }

    public TimeConflictResponse(boolean hasConflict, String message, String conflictType, List<ConflictingSession> conflictingSessions) {
        this.hasConflict = hasConflict;
        this.message = message;
        this.conflictType = conflictType;
        this.conflictingSessions = conflictingSessions;
    }

    // Getters and Setters
    public boolean isHasConflict() {
        return hasConflict;
    }

    public void setHasConflict(boolean hasConflict) {
        this.hasConflict = hasConflict;
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

    public String getConflictType() {
        return conflictType;
    }

    public void setConflictType(String conflictType) {
        this.conflictType = conflictType;
    }

    // Inner class for conflicting session details
    public static class ConflictingSession {
        private Integer id;
        private String title;
        private String chair;
        private String room;
        private LocalDateTime startTime;
        private LocalDateTime endTime;

        public ConflictingSession() {
        }

        public ConflictingSession(Integer id, String title, String chair, String room, LocalDateTime startTime, LocalDateTime endTime) {
            this.id = id;
            this.title = title;
            this.chair = chair;
            this.room = room;
            this.startTime = startTime;
            this.endTime = endTime;
        }

        // Getters and Setters
        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getChair() {
            return chair;
        }

        public void setChair(String chair) {
            this.chair = chair;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
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

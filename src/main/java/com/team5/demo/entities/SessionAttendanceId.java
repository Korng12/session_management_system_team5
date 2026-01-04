package com.team5.demo.entities;

import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

public class SessionAttendanceId implements Serializable {
    
    @Column(name = "participant_id")
    private Long participantId;
    
    @Column(name = "session_id")
    private Long sessionId;
    
    // Default constructor (REQUIRED)
    public SessionAttendanceId() {}
    
    // Constructor with parameters
    public SessionAttendanceId(Long participantId, Long sessionId) {
        this.participantId = participantId;
        this.sessionId = sessionId;
    }
    
    // Getters and Setters
    public Long getParticipantId() {
        return participantId;
    }
    
    public void setParticipantId(Long participantId) {
        this.participantId = participantId;
    }
    
    public Long getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    
    // REQUIRED: equals() method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionAttendanceId that = (SessionAttendanceId) o;
        return Objects.equals(participantId, that.participantId) &&
               Objects.equals(sessionId, that.sessionId);
    }
    
    // REQUIRED: hashCode() method
    @Override
    public int hashCode() {
        return Objects.hash(participantId, sessionId);
    }
    
    @Override
    public String toString() {
        return "SessionAttendanceId{" +
               "participantId=" + participantId +
               ", sessionId=" + sessionId +
               '}';
    }
}
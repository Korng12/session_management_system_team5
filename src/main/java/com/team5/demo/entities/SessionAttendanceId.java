package com.team5.demo.entities;

import java.io.Serializable;
import java.util.Objects;

public class SessionAttendanceId implements Serializable {

    private Long participantId;
    private Long sessionId;

    public SessionAttendanceId() {}

    public SessionAttendanceId(Long participantId, Long sessionId) {
        this.participantId = participantId;
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SessionAttendanceId)) return false;
        SessionAttendanceId that = (SessionAttendanceId) o;
        return Objects.equals(participantId, that.participantId) &&
               Objects.equals(sessionId, that.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(participantId, sessionId);
    }
}

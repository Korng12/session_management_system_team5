// No changes needed for SessionAttendance.java
package com.team5.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_attendance")
@IdClass(SessionAttendanceId.class)
@Data
public class SessionAttendance {

    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @Id
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "participant_id",
        insertable = false,
        updatable = false
    )
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "session_id",
        insertable = false,
        updatable = false
    )
    private Session session;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AttendanceStatus status;

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private User markedBy;

    protected SessionAttendance() {}

    public SessionAttendance(
            Long participantId,
            Long sessionId,
            AttendanceStatus status,
            LocalDateTime markedAt,
            User markedBy
    ) {
        this.participantId = participantId;
        this.sessionId = sessionId;
        this.status = status;
        this.markedAt = markedAt;
        this.markedBy = markedBy;
    }
}

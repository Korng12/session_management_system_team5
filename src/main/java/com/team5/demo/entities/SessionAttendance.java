// No changes needed for SessionAttendance.java
package com.team5.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_attendance")
@Data
@IdClass(SessionAttendanceId.class)
public class SessionAttendance {

    @Id
    @Column(name = "participant_id")
    private Long participantId;

    @Id
    @Column(name = "session_id")
    private Long sessionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private Session session;
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AttendanceStatus status; // PRESENT, ABSENT, REGISTERED

    @Column(name = "marked_at")
    private LocalDateTime markedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private User markedBy;

    protected SessionAttendance() {
    }

    public SessionAttendance(Long participantId, Long sessionId, User participant, Session session,
            AttendanceStatus status, LocalDateTime markedAt, User markedBy) {
        this.participantId = participantId;
        this.sessionId = sessionId;
        this.participant = participant;
        this.session = session;
        this.status = status;
        this.markedAt = markedAt;
        this.markedBy = markedBy;
    }

    // public SessionAttendance(User participant, Session session) {
    //     this.participant = participant;
    //     this.session = session;
    //     this.participantId = participant.getId();
    //     this.sessionId = session.getId();
    //     this.status = AttendanceStatus.ABSENT;
    // }
}

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

    // 🔗 Participant (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private User participant;

    // 🔗 Session
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false)
    private Session session;

    @Column(name = "attended_at")
    private LocalDateTime attendedAt;

    @Column(length = 20)
    private String status; // REGISTERED, PRESENT, LATE, ABSENT

    @PrePersist
    protected void onCreate() {
        this.attendedAt = LocalDateTime.now();
        this.status = "REGISTERED";
        this.participantId = participant.getId();
        this.sessionId = session.getId();
    }
}

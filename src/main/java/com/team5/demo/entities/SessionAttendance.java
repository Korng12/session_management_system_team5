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
    @MapsId("participantId")
    @JoinColumn(name = "participant_id")
    private User participant;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("sessionId")
    @JoinColumn(name = "session_id")
    private Session session;

    @Column(length = 20)
    private String status; // PRESENT, ABSENT, REGISTERED

    public SessionAttendance() {
        this.status = "REGISTERED";
    }

    public SessionAttendance(User participant, Session session) {
        this.participant = participant;
        this.session = session;
        this.participantId = participant.getId();
        this.sessionId = session.getId();
        this.status = "REGISTERED";
    }

    public void setAttendedAt(LocalDateTime now) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setAttendedAt'");
    }

    
}

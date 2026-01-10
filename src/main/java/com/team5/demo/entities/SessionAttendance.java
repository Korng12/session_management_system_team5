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
    @Column(name = "participant_id", insertable = false, updatable = false)
    private Long participantId;
    
    @Id
    @Column(name = "session_id", insertable = false, updatable = false)
    private Long sessionId;
    
    // JPA Relationships
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", referencedColumnName = "id", insertable = false, updatable = false)
    private User participant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Session session;
    
    // @Column(name = "attended_at")
    // private LocalDateTime attendedAt;
    
    // @Column(name = "check_in_time")
    // private LocalDateTime checkInTime;
    
    // @Column(name = "check_out_time")
    // private LocalDateTime checkOutTime;
    
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private AttendanceStatus status; // PRESENT, ABSENT
     @Column(name = "marked_at")
    private LocalDateTime markedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "marked_by")
    private User markedBy;
    // Constructors
    // public SessionAttendance() {
    //     this.attendedAt = LocalDateTime.now();
    //     this.status = AttendanceStatus.ABSENT;
    // }
      public SessionAttendance() {
    
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
    
    
}
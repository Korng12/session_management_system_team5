package com.team5.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "session_attendance")
@Data
@IdClass(SessionAttendanceId.class)
public class SessionAttendance {
    @Id
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private User participant;
    
    @Id
    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
}

class SessionAttendanceId implements java.io.Serializable {
    private Long participant;
    private Long session;
}
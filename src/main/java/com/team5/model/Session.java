package com.team5.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "sessions")
@Data
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 50)
    private String title;
    
    @Column(length = 50)
    private String location;
    
    @ManyToOne
    @JoinColumn(name = "chair_id")
    private User chair;
    
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;
    
    @ManyToOne
    @JoinColumn(name = "conference_id")
    private Conference conference;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @OneToMany(mappedBy = "session")
    private List<SessionAttendance> attendances;
}
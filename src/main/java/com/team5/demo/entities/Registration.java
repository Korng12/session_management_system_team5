package com.team5.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "registrations")
@Data
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "participant_id")
    private Long participantId;
    
    @Column(name = "conference_id")
    private Long conferenceId;
    
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
    
    @Column(length = 20)
    private String status; // "PENDING", "CONFIRMED", "CANCELLED"
}
package com.team5.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "registrations")
public class Registration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "participant_id", nullable = false)
    private Long participantId;
    
    @Column(name = "conference_id", nullable = false)
    private Long conferenceId;
    
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;
    
    @Column(nullable = false)
    private String status;
    
    @PrePersist
    protected void onCreate() {
        registeredAt = LocalDateTime.now();
    }
}

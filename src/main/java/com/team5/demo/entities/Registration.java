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
    
    //JPA Relationship with User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User participant;  // Must be named 'participant'
    
    //  JPA Relationship with Conference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;  // Must be named 'conference'
    
    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
    
    @Column(length = 20)
    private String status; // "PENDING", "CONFIRMED", "CANCELLED"
    
    // Constructors
    public Registration() {
        this.registeredAt = LocalDateTime.now();
        this.status = "PENDING";
    }
    
    // With User and Conference entities
    public Registration(User participant, Conference conference) {
        this();
        this.participant = participant;
        this.conference = conference;
    }
    
    public Registration(User participant, Conference conference, String status) {
        this(participant, conference);
        this.status = status;
    }
    
    
    public boolean isCancelled() {
        return "CANCELLED".equals(status);
    }
}
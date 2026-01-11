package com.team5.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

import com.team5.demo.enums.RegistrationStatus;

@Data
@Entity
@Table(name = "registrations")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private User participant;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    @EqualsAndHashCode.Exclude
    private Conference conference;
    
    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    // private String status = "CONFIRMED";
    private RegistrationStatus status;
    @PrePersist
    protected void onCreate() {
        if (registeredAt == null) {
            registeredAt = LocalDateTime.now();
        }
    }
}


// // Participant (User)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "participant_id", nullable = false)
    // private User participant;

    // //  Conference
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "conference_id", nullable = false)
    // private Conference conference;

    // @Column(name = "registered_at", nullable = false)
    // private LocalDateTime registeredAt;

    // @Column(length = 20, nullable = false)
    // private String status; // PENDING, CONFIRMED, CANCELLED

    // /* ===================== CONSTRUCTORS ===================== */

    // public Registration() {
    //     this.registeredAt = LocalDateTime.now();
    //     this.status = "PENDING";
    // }

    // public Registration(User participant, Conference conference) {
    //     this();
    //     this.participant = participant;
    //     this.conference = conference;
    // }

    // public Registration(User participant, Conference conference, String status) {
    //     this(participant, conference);
    //     this.status = status;
    // }


    // public boolean isCancelled() {
    //     return "CANCELLED".equalsIgnoreCase(status);
    // }
    
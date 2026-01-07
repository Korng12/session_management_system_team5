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

    // Participant (User)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id", nullable = false)
    private User participant;

    //  Conference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conference_id", nullable = false)
    private Conference conference;

    @Column(name = "registered_at", nullable = false)
    private LocalDateTime registeredAt;

    @Column(length = 20, nullable = false)
    private String status; // PENDING, CONFIRMED, CANCELLED

    /* ===================== CONSTRUCTORS ===================== */

    public Registration() {
        this.registeredAt = LocalDateTime.now();
        this.status = "PENDING";
    }

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
        return "CANCELLED".equalsIgnoreCase(status);
    }
}

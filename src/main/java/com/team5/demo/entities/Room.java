package com.team5.demo.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "rooms")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private Integer capacity;

    // Optional: keep only if you really need it
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Session> sessions;
}

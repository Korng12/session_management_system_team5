package com.team5.demo.model;

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
    
    @Column(nullable = false, length = 30)
    private String name;
    
    private Integer capacity;
    
    @OneToMany(mappedBy = "room")
    private List<Session> sessions;
}
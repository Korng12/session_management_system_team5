package com.team5.demo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 50)
    private String name;
    
    @Column(length = 50)
    private String email;
    
    @Column(length = 255)
    private String password;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "participant")
    private List<Registration> registrations;
    
    @OneToMany(mappedBy = "participant")
    private List<SessionAttendance> sessionAttendances;
}
package com.team5.demo.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity // Tells Hibernate this is a table
@Table(name = "users") // Optional: Custom table name
public class User {
    @Id // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment
    private Long id;

    @Column(nullable = false)
    private String name;

    private String email;

    // IMPORTANT: Required empty constructor
    protected User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
    // Getters and Setters...
}

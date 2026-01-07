package com.team5.demo.dto;

import java.util.Set;

public class AdminUserDTO {
    private Long id;
    private String name;
    private String email;
    private Set<String> roles;

    public AdminUserDTO(Long id, String name, String email, Set<String> roles) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.roles = roles;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Set<String> getRoles() { return roles; }
}

package com.team5.demo.dto;

import java.util.Set;

public class AssignRolesRequest {
    private Set<String> roles;

    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
}

package com.team5.demo.dto;

public class SessionDependenciesResponse {
    private boolean hasDependencies;
    private int registrationCount;
    private String message;
    private boolean canDelete;

    public SessionDependenciesResponse() {
    }

    public SessionDependenciesResponse(boolean hasDependencies, int registrationCount, String message, boolean canDelete) {
        this.hasDependencies = hasDependencies;
        this.registrationCount = registrationCount;
        this.message = message;
        this.canDelete = canDelete;
    }

    // Getters and Setters
    public boolean isHasDependencies() {
        return hasDependencies;
    }

    public void setHasDependencies(boolean hasDependencies) {
        this.hasDependencies = hasDependencies;
    }

    public int getRegistrationCount() {
        return registrationCount;
    }

    public void setRegistrationCount(int registrationCount) {
        this.registrationCount = registrationCount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isCanDelete() {
        return canDelete;
    }

    public void setCanDelete(boolean canDelete) {
        this.canDelete = canDelete;
    }
}

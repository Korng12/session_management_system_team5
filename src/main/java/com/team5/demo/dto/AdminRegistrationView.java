package com.team5.demo.dto;

public class AdminRegistrationView {

    private final Long registrationId;
    private final String userEmail;
    private final String conferenceTitle;
    private final String sessionTitle;
    private final String status;

    public AdminRegistrationView(
            Long registrationId,
            String userEmail,
            String conferenceTitle,
            String sessionTitle,
            String status
    ) {
        this.registrationId = registrationId;
        this.userEmail = userEmail;
        this.conferenceTitle = conferenceTitle;
        this.sessionTitle = sessionTitle;
        this.status = status;
    }

    public Long getRegistrationId() { return registrationId; }
    public String getUserEmail() { return userEmail; }
    public String getConferenceTitle() { return conferenceTitle; }
    public String getSessionTitle() { return sessionTitle; }
    public String getStatus() { return status; }
}

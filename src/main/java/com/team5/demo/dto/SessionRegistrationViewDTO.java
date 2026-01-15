package com.team5.demo.dto;

import java.time.LocalDateTime;

import com.team5.demo.entities.SessionStatus;

public record SessionRegistrationViewDTO(
    Long registrationId,
    String participantName,
    String sessionTitle,
    SessionStatus sessionStatus,
    LocalDateTime registeredAt
) {}

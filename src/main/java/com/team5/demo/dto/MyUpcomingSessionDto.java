package com.team5.demo.dto;

import java.time.LocalDateTime;

import com.team5.demo.entities.SessionStatus;

public record MyUpcomingSessionDto(
    Long sessionId,
    String title,
    String conferenceTitle,
    LocalDateTime startTime,
    LocalDateTime endTime,
    SessionStatus sessionStatus
) {}

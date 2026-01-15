package com.team5.demo.dto.chair;

import java.time.LocalDateTime;

import com.team5.demo.entities.SessionStatus;

public record SessionViewDTO(
    Long id,
    String title,
    SessionStatus status,
    LocalDateTime startTime,
    LocalDateTime endTime
) {}

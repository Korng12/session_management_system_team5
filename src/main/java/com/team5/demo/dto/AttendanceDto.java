package com.team5.demo.dto;

import com.team5.demo.entities.AttendanceStatus;

public record AttendanceDto(
    Long participantId,
    String name,
    String email,
    AttendanceStatus attendance
) {}

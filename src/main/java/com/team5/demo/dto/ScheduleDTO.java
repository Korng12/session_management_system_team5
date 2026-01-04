package com.team5.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
    private Long sessionId;
    private String title;
    private String location;
    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
package com.team5.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TimeSlotDTO {
    private Long roomId;
    private String roomName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean available;
}
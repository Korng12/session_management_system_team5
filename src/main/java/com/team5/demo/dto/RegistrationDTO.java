package com.team5.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegistrationDTO {
    private Long id;
    private Long sessionId;
    private Long userId;
    private LocalDateTime registrationTime;
    private String status;
}
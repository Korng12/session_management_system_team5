package com.team5.demo.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegistrationDTO {
    private Long id;
    private Long participantId;
    private String participantName;
    private Long conferenceId;
    private String conferenceTitle;
    private LocalDateTime registeredAt;
    private String status;
}
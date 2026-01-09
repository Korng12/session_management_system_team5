package com.team5.demo.dto;
import lombok.Data;
@Data
public class RegistrationRequest {
     private Long participantId;
    private Long conferenceId;
    private String status; // optional
}

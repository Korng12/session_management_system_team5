package com.team5.demo.dto;

<<<<<<< HEAD
public class RegistrationDTO {
    
}
=======
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
>>>>>>> 55a6064 (Implement Secondary module lead-Logistics and Registration)

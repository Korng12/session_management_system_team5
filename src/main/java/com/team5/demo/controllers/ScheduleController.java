package com.team5.demo.controllers;

import com.team5.demo.dto.ScheduleDTO;  
import com.team5.demo.service.ScheduleService;  
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    
    private final ScheduleService scheduleService;
    
    @GetMapping("/my-schedule/{userId}")
    public ResponseEntity<List<ScheduleDTO>> getMySchedule(@PathVariable Long userId) {
        List<ScheduleDTO> schedule = scheduleService.getUserSchedule(userId);
        return ResponseEntity.ok(schedule);
    }
    
    @GetMapping("/check-registration")
    public ResponseEntity<Boolean> checkRegistration(
            @RequestParam Long userId,
            @RequestParam Long conferenceId) {
        boolean isRegistered = scheduleService.isUserRegisteredForConference(userId, conferenceId);
        return ResponseEntity.ok(isRegistered);
    }
}
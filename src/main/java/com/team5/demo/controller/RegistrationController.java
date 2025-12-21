package com.team5.demo.controller;

import com.team5.demo.model.Registration;  
import com.team5.demo.service.RegistrationService; 
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")
@RequiredArgsConstructor
public class RegistrationController {
    
    private final RegistrationService registrationService;
    
    @PostMapping("/register")
    public ResponseEntity<Registration> registerForConference(
            @RequestParam Long userId,
            @RequestParam Long conferenceId) {
        Registration registration = registrationService.registerForConference(userId, conferenceId);
        return ResponseEntity.ok(registration);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Registration>> getUserRegistrations(@PathVariable Long userId) {
        List<Registration> registrations = registrationService.getUserRegistrations(userId);
        return ResponseEntity.ok(registrations);
    }
    
    @DeleteMapping("/{registrationId}/cancel")
    public ResponseEntity<Void> cancelRegistration(@PathVariable Long registrationId) {
        registrationService.cancelRegistration(registrationId);
        return ResponseEntity.noContent().build();
    }
}
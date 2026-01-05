package com.team5.demo.controllers;

import com.team5.demo.entities.Registration;
import com.team5.demo.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")

@RequiredArgsConstructor
public class RegistrationController {
    
    private final RegistrationService registrationService;
    
    @PostMapping("/register")
    public ResponseEntity<?> registerForConference(
            @RequestParam Long userId,
            @RequestParam Long conferenceId) {
        try {
            Registration registration = registrationService.registerForConference(userId, conferenceId);
            return ResponseEntity.ok(registration);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserRegistrations(@PathVariable Long userId) {
        try {
            List<Registration> registrations = registrationService.getUserRegistrations(userId);
            return ResponseEntity.ok(registrations);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/{registrationId}")
    public ResponseEntity<?> getRegistration(@PathVariable Long registrationId) {
        try {
            Registration registration = registrationService.getRegistration(registrationId);
            return ResponseEntity.ok(registration);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PutMapping("/{registrationId}/cancel")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long registrationId) {
        try {
            registrationService.cancelRegistration(registrationId);
            return ResponseEntity.ok("Registration cancelled successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
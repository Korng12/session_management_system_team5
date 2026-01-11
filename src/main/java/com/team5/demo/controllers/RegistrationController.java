package com.team5.demo.controllers;

import com.team5.demo.entities.Registration;
import com.team5.demo.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/registrations")

@RequiredArgsConstructor
public class RegistrationController {
    
    private final RegistrationService registrationService;
    
    // @PostMapping("/register")
    //     public ResponseEntity<?> registerForConference(
    //         @RequestParam("userId") Long userId,
    //         @RequestParam("conferenceId") Long conferenceId) {
    //     try {
    //         Registration registration = registrationService.registerForConference(userId, conferenceId);
    //         return ResponseEntity.ok(registration);
    //     } catch (RuntimeException e) {
    //         return ResponseEntity.badRequest().body(e.getMessage());
    //     }
    // }
    @PostMapping("/register")

    public ResponseEntity<?> registerForConference(
        @RequestParam("conferenceId") Long conferenceId,
        Authentication authentication) {

        String email = authentication.getName();

        Registration reg =
            registrationService.registerForConference(email, conferenceId);

        return ResponseEntity.ok(reg);
    }


    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserRegistrations(@PathVariable("userId") Long userId) {
        try {
            List<Registration> registrations = registrationService.getUserRegistrations(userId);
            return ResponseEntity.ok(registrations);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @GetMapping("/{registrationId}")
    public ResponseEntity<?> getRegistration(@PathVariable("registrationId") Long registrationId) {
        try {
            Registration registration = registrationService.getRegistration(registrationId);
            return ResponseEntity.ok(registration);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    
    @PutMapping("/{registrationId}/cancel")
    public ResponseEntity<?> cancelRegistration(@PathVariable("registrationId") Long registrationId) {
        try {
            registrationService.cancelRegistration(registrationId);
            return ResponseEntity.ok("Registration cancelled successfully");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
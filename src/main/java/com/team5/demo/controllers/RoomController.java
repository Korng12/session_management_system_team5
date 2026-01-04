package com.team5.demo.controllers;

import com.team5.demo.entities.Registration;
import com.team5.demo.services.RegistrationService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class RoomController {

    private final RegistrationService registrationService;

    @PostMapping("/{roomId}/register")
    public ResponseEntity<Registration> registerRoom(
            @PathVariable Long roomId,
            @RequestParam Long userId) {

        Registration registration =
                registrationService.registerForConference(userId, roomId);

        return ResponseEntity.ok(registration);
    }
}

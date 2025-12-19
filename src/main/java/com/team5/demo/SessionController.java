// src/main/java/com/team5/controller/SessionController.java
package com.team5.controller;

import com.team5.entity.Session;
import com.team5.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    @Autowired
    private SessionService sessionService;

    @GetMapping
    public List<Session> getAllSessions() {
        return sessionService.getAllSessions();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Session> getSessionById(@PathVariable Long id) {
        return ResponseEntity.ok(sessionService.getSessionById(id));
    }

    @PostMapping
    public Session createSession(@RequestBody Session session) {
        return sessionService.createSession(session);
    }

    @PutMapping("/{id}")
    public Session updateSession(@PathVariable Long id, @RequestBody Session sessionDetails) {
        return sessionService.updateSession(id, sessionDetails);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/upcoming")
    public List<Session> getUpcomingSessions() {
        return sessionService.getUpcomingSessions();
    }

    @GetMapping("/status/{status}")
    public List<Session> getSessionsByStatus(@PathVariable String status) {
        return sessionService.getSessionsByStatus(Session.SessionStatus.valueOf(status.toUpperCase()));
    }
}
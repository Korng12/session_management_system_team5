package com.team5.demo.controllers;

import com.team5.demo.model.Session;
import com.team5.demo.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
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
    
    // Soft delete specific endpoints
    @PostMapping("/{id}/restore")
    public ResponseEntity<Session> restoreSession(@PathVariable Long id) {
        Session restoredSession = sessionService.restoreSession(id);
        return ResponseEntity.ok(restoredSession);
    }
    
    @DeleteMapping("/{id}/hard")
    public ResponseEntity<?> hardDeleteSession(@PathVariable Long id) {
        sessionService.hardDeleteSession(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/deleted")
    public List<Session> getDeletedSessions() {
        return sessionService.getDeletedSessions();
    }
    
    @GetMapping("/deleted/paginated")
    public Page<Session> getDeletedSessionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return sessionService.getDeletedSessionsPaginated(page, size);
    }

    @GetMapping("/upcoming")
    public List<Session> getUpcomingSessions() {
        return sessionService.getUpcomingSessions();
    }

    @GetMapping("/status/{status}")
    public List<Session> getSessionsByStatus(@PathVariable String status) {
        return sessionService.getSessionsByStatus(Session.SessionStatus.valueOf(status.toUpperCase()));
    }
    
    // Session Chair Assignment Endpoints
    @PostMapping("/{sessionId}/assign-chair/{chairId}")
    public Session assignChairToSession(@PathVariable Long sessionId, @PathVariable Long chairId) {
        return sessionService.assignChairToSession(sessionId, chairId);
    }
    
    @PostMapping("/{sessionId}/remove-chair")
    public Session removeChairFromSession(@PathVariable Long sessionId) {
        return sessionService.removeChairFromSession(sessionId);
    }
    
    @GetMapping("/chair/{chairId}")
    public List<Session> getSessionsByChair(@PathVariable Long chairId) {
        return sessionService.getSessionsByChair(chairId);
    }
    
    // Pagination endpoints
    @GetMapping("/paginated")
    public Page<Session> getAllSessionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        return sessionService.getAllSessionsPaginated(page, size, sortBy, sortDir);
    }
    
    @GetMapping("/status/{status}/paginated")
    public Page<Session> getSessionsByStatusPaginated(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return sessionService.getSessionsByStatusPaginated(Session.SessionStatus.valueOf(status.toUpperCase()), page, size);
    }
    
    @GetMapping("/chair/{chairId}/paginated")
    public Page<Session> getSessionsByChairPaginated(
            @PathVariable Long chairId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return sessionService.getSessionsByChairPaginated(chairId, page, size);
    }
    
    @GetMapping("/search")
    public Page<Session> searchSessionsByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return sessionService.searchSessionsByTitle(title, page, size);
    }
    
    @GetMapping("/date-range/paginated")
    public Page<Session> getSessionsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return sessionService.getSessionsByDateRange(startDate, endDate, page, size);
    }
    
    @GetMapping("/chair/{chairId}/status/{status}/paginated")
    public Page<Session> getSessionsByChairAndStatus(
            @PathVariable Long chairId,
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return sessionService.getSessionsByChairAndStatus(chairId, Session.SessionStatus.valueOf(status.toUpperCase()), page, size);
    }
}

// src/main/java/com/team5/service/SessionService.java
package com.team5.demo;

import com.team5.entity.User;
import com.team5.exception.ResourceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    public List<Session> getAllSessions() {
        return sessionRepository.findAll();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
    }

    public Session createSession(Session session) {
        // Validate session time
        if (session.getEndTime().isBefore(session.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Check for session conflicts
        if (sessionRepository.existsByTitleAndStartTime(session.getTitle(), session.getStartTime())) {
            throw new IllegalStateException("A session with this title already exists at the given time");
        }

        return sessionRepository.save(session);
    }

    public Session updateSession(Long id, Session sessionDetails) {
        Session session = getSessionById(id);
        
        session.setTitle(sessionDetails.getTitle());
        session.setDescription(sessionDetails.getDescription());
        session.setStartTime(sessionDetails.getStartTime());
        session.setEndTime(sessionDetails.getEndTime());
        session.setCapacity(sessionDetails.getCapacity());
        session.setStatus(sessionDetails.getStatus());
        
        if (sessionDetails.getChair() != null) {
            session.setChair(sessionDetails.getChair());
        }
        
        return sessionRepository.save(session);
    }

    public void deleteSession(Long id) {
        Session session = getSessionById(id);
        sessionRepository.delete(session);
    }

    public List<Session> getUpcomingSessions() {
        return sessionRepository.findByStartTimeAfter(LocalDateTime.now());
    }

    public List<Session> getSessionsByStatus(Session.SessionStatus status) {
        return sessionRepository.findByStatus(status);
    }
}
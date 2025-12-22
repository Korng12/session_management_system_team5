package com.team5.demo.service;

import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionStatus;
import com.team5.demo.entities.User;
import com.team5.demo.exception.ResourceNotFoundException;
import com.team5.demo.repositories.SessionRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        softDeleteSession(session);
    }
    
    public void softDeleteSession(Session session) {
        session.setIsDeleted(true);
        session.setDeletedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }
    
    public Session restoreSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        
        if (!session.getIsDeleted()) {
            throw new IllegalStateException("Session is not deleted and cannot be restored");
        }
        
        session.setIsDeleted(false);
        session.setDeletedAt(null);
        return sessionRepository.save(session);
    }
    
    public void hardDeleteSession(Long id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + id));
        sessionRepository.delete(session);
    }
    
    public Page<Session> getDeletedSessions(Pageable pageable) {
        return sessionRepository.findDeletedSessions(pageable);
    }
    
    public Page<Session> getDeletedSessionsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("deletedAt").descending());
        return sessionRepository.findDeletedSessions(pageable);
    }

    public List<Session> getUpcomingSessions() {
        return sessionRepository.findByStartTimeAfter(LocalDateTime.now());
    }

    public List<Session> getSessionsByStatus(SessionStatus status) {
        return sessionRepository.findByStatus(status);
    }
    
    // Session Chair Assignment Methods
    public Session assignChairToSession(Long sessionId, Long chairId) {
        Session session = getSessionById(sessionId);
        
        // In a real application, you would validate that the user exists and has appropriate permissions
        // For now, we'll assume the User entity exists and create a simple reference
        User chair = new User();
        chair.setId(chairId);
        
        session.setChair(chair);
        return sessionRepository.save(session);
    }
    
    public Session removeChairFromSession(Long sessionId) {
        Session session = getSessionById(sessionId);
        session.setChair(null);
        return sessionRepository.save(session);
    }
    
    @Deprecated
    public List<Session> getSessionsByChair(Long chairId) {
        return sessionRepository.findByChairId(chairId, PageRequest.of(0, Integer.MAX_VALUE, Sort.by("startTime").ascending())).getContent();
    }
    
    // Pagination methods
    public Page<Session> getAllSessionsPaginated(int page, int size, String sortBy, String sortDir) {
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        return sessionRepository.findAll(pageable);
    }
    
    public Page<Session> getSessionsByStatusPaginated(SessionStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return sessionRepository.findByStatus(status, pageable);
    }
    
    public Page<Session> getSessionsByChairPaginated(Long chairId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return sessionRepository.findByChairId(chairId, pageable);
    }
    
    public Page<Session> searchSessionsByTitle(String title, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("title").ascending());
        return sessionRepository.findByTitleContaining(title, pageable);
    }
    
    public Page<Session> getSessionsByDateRange(LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return sessionRepository.findByStartTimeBetween(startDate, endDate, pageable);
    }
    
    public Page<Session> getSessionsByChairAndStatus(Long chairId, SessionStatus status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("startTime").ascending());
        return sessionRepository.findByChairIdAndStatus(chairId, status, pageable);
    }
}

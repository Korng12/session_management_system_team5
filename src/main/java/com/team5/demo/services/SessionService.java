package com.team5.demo.services;

import com.team5.demo.entities.Session;
import com.team5.demo.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    // For admin schedule table
    public List<Session> getAllSessions() {
        return sessionRepository.findAllWithRoom();
    }

    public Session getSessionById(Long id) {
        return sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));
    }

    public void deleteSession(Long id) {
        sessionRepository.deleteById(id);
    }

    // ✅ SAFE UPDATE (ONLY TITLE)
    public void updateTitle(Long id, String title) {

        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setTitle(title);

        sessionRepository.save(session);
    }
}

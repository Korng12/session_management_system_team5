package com.team5.demo.services;

import com.team5.demo.entities.Session;
import com.team5.demo.repositories.SessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SessionService {

    private final SessionRepository sessionRepository;

    // ✅ For Admin - Manage Schedule table
    public List<Session> getAllSessions() {
        return sessionRepository.findAllWithRoom();
    }

    // ✅ Safe delete
    public void deleteSession(Long id) {
        if (!sessionRepository.existsById(id)) {
            throw new RuntimeException("Session not found with id: " + id);
        }
        sessionRepository.deleteById(id);
    }

    // ✅ Safe update (title only)
    public void updateTitle(Long id, String title) {

        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + id));

        session.setTitle(title);
        // No need to call save() explicitly, @Transactional will persist changes
    }
}

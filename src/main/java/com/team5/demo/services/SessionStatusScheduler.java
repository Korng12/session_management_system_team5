package com.team5.demo.services;

import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionStatus;
import com.team5.demo.repositories.SessionRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SessionStatusScheduler {

    private final SessionRepository sessionRepository;

    public SessionStatusScheduler(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    // Runs every minute
    @Scheduled(fixedRate = 60000)
    public void updateSessionStatus() {
        LocalDateTime now = LocalDateTime.now();

        List<Session> sessions = sessionRepository.findAll();

        for (Session session : sessions) {

            if (session.isDeleted()) {
                continue;
            }

            if (now.isBefore(session.getStartTime())) {
                session.setStatus(SessionStatus.SCHEDULED);

            } else if (now.isAfter(session.getStartTime())
                    && now.isBefore(session.getEndTime())) {
                session.setStatus(SessionStatus.ONGOING);

            } else if (now.isAfter(session.getEndTime())) {
                session.setStatus(SessionStatus.COMPLETED);
            }
        }

        sessionRepository.saveAll(sessions);
    }
}

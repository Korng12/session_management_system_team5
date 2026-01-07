package com.team5.demo.services;

import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.SessionAttendanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SessionAttendanceService {

    private final SessionAttendanceRepository repo;

    public SessionAttendanceService(SessionAttendanceRepository repo) {
        this.repo = repo;
    }

    public List<SessionAttendance> findAll() {
        return repo.findAll();
    }

    public List<SessionAttendance> findBySession(Long sessionId) {
        return repo.findBySession_Id(sessionId);
    }

    public SessionAttendance register(User participant, Session session) {
        SessionAttendance attendance = new SessionAttendance();
        attendance.setParticipant(participant);
        attendance.setSession(session);
        return repo.save(attendance);
    }

    public void remove(Long participantId, Long sessionId) {
        repo.deleteByParticipantIdAndSessionId(participantId, sessionId);
    }
}

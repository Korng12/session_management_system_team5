package com.team5.demo.repositories;

import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionAttendanceId;
import com.team5.demo.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SessionAttendanceRepository extends JpaRepository<SessionAttendance, SessionAttendanceId> {
    
    /**
     * Find all attendance records for a specific session
     */
    List<SessionAttendance> findBySessionId(Long sessionId);
    List <SessionAttendance> findByParticipant(User user);
    
    /**
     * Find all attendance records for a specific participant
     */
    List<SessionAttendance> findByParticipantId(Long participantId);
    
    /**
     * Find a specific attendance record
     */
    Optional<SessionAttendance> findByParticipantIdAndSessionId(Long participantId, Long sessionId);
    Optional<SessionAttendance> findByParticipantAndSession(User participant,Session session);
    /**
     * Check if a participant attended a session
     */
    boolean existsByParticipantIdAndSessionId(Long participantId, Long sessionId);
}

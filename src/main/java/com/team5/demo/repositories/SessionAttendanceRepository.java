package com.team5.demo.repositories;

import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionAttendanceId;
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
    
    /**
     * Find all attendance records for a specific participant
     */
    List<SessionAttendance> findByParticipantId(Long participantId);
    
    /**
     * Find a specific attendance record
     */
    Optional<SessionAttendance> findByParticipantIdAndSessionId(Long participantId, Long sessionId);
    
    /**
     * Check if a participant attended a session
     */
    boolean existsByParticipantIdAndSessionId(Long participantId, Long sessionId);
}

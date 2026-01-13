package com.team5.demo.repositories;

import com.team5.demo.dto.ScheduleDTO;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionAttendanceId;
import com.team5.demo.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionAttendanceRepository
        extends JpaRepository<SessionAttendance, SessionAttendanceId> {

    /* ===================== BASIC QUERIES ===================== */

    List<SessionAttendance> findBySessionId(Long sessionId);
    List<SessionAttendance> findByParticipant(User user);
    List<SessionAttendance> findByParticipantId(Long participantId);
    Optional<SessionAttendance> findByParticipantIdAndSessionId(Long participantId, Long sessionId);
    Optional<SessionAttendance> findByParticipantAndSession(User participant, Session session);
    boolean existsByParticipantIdAndSessionId(Long participantId, Long sessionId);

    @Query("""
        SELECT sa
        FROM SessionAttendance sa
        JOIN FETCH sa.participant
        JOIN FETCH sa.session
    """)
    List<SessionAttendance> findAllWithRelations();

    @Query("""
        SELECT sa
        FROM SessionAttendance sa
        JOIN FETCH sa.session
        JOIN FETCH sa.participant p
        WHERE p.email = :email
    """)
    List<SessionAttendance> findMySchedule(@Param("email") String email);
}

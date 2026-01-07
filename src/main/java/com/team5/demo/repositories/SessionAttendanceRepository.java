package com.team5.demo.repositories;

import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionAttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionAttendanceRepository
        extends JpaRepository<SessionAttendance, SessionAttendanceId> {

    List<SessionAttendance> findBySession_Id(Long sessionId);

    List<SessionAttendance> findByParticipant_Id(Long participantId);

    void deleteByParticipantIdAndSessionId(Long participantId, Long sessionId);
}

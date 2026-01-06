package com.team5.demo.repositories;

import com.team5.demo.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {

    // ✅ FIXED: LEFT JOIN FETCH (shows sessions even if room is null)
    @Query("""
        SELECT DISTINCT s FROM Session s
        LEFT JOIN FETCH s.room
        ORDER BY s.startTime
    """)
    List<Session> findAllWithRoom();

    // ✅ Simple derived query
    List<Session> findByRoom_Id(Long roomId);

    // ✅ Prevent room double booking
    @Query("""
        SELECT s FROM Session s
        WHERE s.room.id = :roomId
          AND s.startTime < :endTime
          AND s.endTime > :startTime
    """)
    List<Session> findOverlappingSessions(
            @Param("roomId") Long roomId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    // ✅ User / Chair schedule
    @Query("""
        SELECT DISTINCT s FROM Session s
        LEFT JOIN FETCH s.room
        JOIN s.attendances a
        WHERE a.participant.id = :userId
        ORDER BY s.startTime
    """)
    List<Session> findSessionsByParticipantId(
            @Param("userId") Long userId
    );
}

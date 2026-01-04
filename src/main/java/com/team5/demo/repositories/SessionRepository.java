package com.team5.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team5.demo.entities.Session;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByRoomId(Long roomId);
    
    @Query("SELECT s FROM Session s WHERE s.room.id = :roomId AND " +
           "((s.startTime BETWEEN :startTime AND :endTime) OR " +
           "(s.endTime BETWEEN :startTime AND :endTime) OR " +
           "(:startTime BETWEEN s.startTime AND s.endTime))")
    List<Session> findOverlappingSessions(@Param("roomId") Long roomId,
                                          @Param("startTime") LocalDateTime startTime,
                                          @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT s FROM Session s JOIN s.attendances a " +
           "WHERE a.participant.id = :userId " +
           "ORDER BY s.startTime")
    List<Session> findSessionsByParticipantId(@Param("userId") Long userId);
}
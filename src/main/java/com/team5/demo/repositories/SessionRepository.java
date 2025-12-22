package com.team5.demo.repositories;

import com.team5.demo.model.Session;  // FIXED: Changed from com.conference
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
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
    
    // Find sessions by status
    List<Session> findByStatus(com.team5.demo.entities.SessionStatus status);
    
    // Find sessions by status with pagination
    Page<Session> findByStatus(com.team5.demo.entities.SessionStatus status, Pageable pageable);
    
    // Find sessions by chair ID with pagination
    Page<Session> findByChairId(Long chairId, Pageable pageable);
    
    // Find sessions by chair ID and status with pagination
    Page<Session> findByChairIdAndStatus(Long chairId, com.team5.demo.entities.SessionStatus status, Pageable pageable);
    
    // Find sessions by title containing with pagination
    Page<Session> findByTitleContaining(String title, Pageable pageable);
    
    // Find sessions by date range with pagination
    Page<Session> findByStartTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    // Find deleted sessions with pagination
    @Query("SELECT s FROM Session s WHERE s.isDeleted = true")
    Page<Session> findDeletedSessions(Pageable pageable);
    
    // Find upcoming sessions (start time after now)
    List<Session> findByStartTimeAfter(LocalDateTime now);
}
package com.team5.demo.repositories;

import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionStatus;
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
    // Find all sessions for a specific conference
    List<Session> findByConferenceId(Long conferenceId);

    // Find all sessions for a specific room
    List<Session> findByRoomId(Long roomId);

    // Find all sessions with a specific chair
    List<Session> findByChairId(Long chairId);
    
    // Find sessions by chair with pagination
    Page<Session> findByChairId(Long chairId, Pageable pageable);
    
    // Find sessions by status
    List<Session> findByStatus(SessionStatus status);
    
    // Check if room is available for a time period
    @Query("SELECT CASE WHEN COUNT(s) = 0 THEN true ELSE false END FROM Session s " +
           "WHERE s.room.id = :roomId " +
           "AND s.deleted = false " +
           "AND ((:excludeId IS NULL) OR (s.id != :excludeId)) " +
           "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    boolean isRoomAvailable(@Param("roomId") Long roomId, 
                           @Param("startTime") LocalDateTime startTime,
                           @Param("endTime") LocalDateTime endTime,
                           @Param("excludeId") Long excludeId);
    
    // Check if chair is available for a time period
    @Query("SELECT CASE WHEN COUNT(s) = 0 THEN true ELSE false END FROM Session s " +
           "WHERE s.chair.id = :chairId " +
           "AND s.deleted = false " +
           "AND ((:excludeId IS NULL) OR (s.id != :excludeId)) " +
           "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    boolean isChairAvailable(@Param("chairId") Long chairId,
                            @Param("startTime") LocalDateTime startTime,
                            @Param("endTime") LocalDateTime endTime,
                            @Param("excludeId") Long excludeId);
    
    // Find conflicting sessions for a room
    @Query("SELECT s FROM Session s " +
           "WHERE s.room.id = :roomId " +
           "AND s.deleted = false " +
           "AND ((:excludeId IS NULL) OR (s.id != :excludeId)) " +
           "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Session> findConflictingSessions(@Param("roomId") Long roomId,
                                         @Param("startTime") LocalDateTime startTime,
                                         @Param("endTime") LocalDateTime endTime,
                                         @Param("excludeId") Long excludeId);
    
    // Find chair conflicts
    @Query("SELECT s FROM Session s " +
           "WHERE s.chair.id = :chairId " +
           "AND s.deleted = false " +
           "AND ((:excludeId IS NULL) OR (s.id != :excludeId)) " +
           "AND ((s.startTime < :endTime AND s.endTime > :startTime))")
    List<Session> findChairConflicts(@Param("chairId") Long chairId,
                                     @Param("startTime") LocalDateTime startTime,
                                     @Param("endTime") LocalDateTime endTime,
                                     @Param("excludeId") Long excludeId);
    
    // Find all sessions including soft deleted ones
    @Query("SELECT s FROM Session s")
    List<Session> findAllIncludingDeleted();
}

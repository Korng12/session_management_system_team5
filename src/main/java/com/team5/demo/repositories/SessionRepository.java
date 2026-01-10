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
       // Find all sessions for a specific conference (excluding soft-deleted)
       List<Session> findByConferenceIdAndDeletedFalse(Long conferenceId);

       // Find all sessions for a specific room (excluding soft-deleted)
       List<Session> findByRoomIdAndDeletedFalse(Long roomId);

       // Find all sessions with a specific chair (excluding soft-deleted)
       List<Session> findByChairIdAndDeletedFalse(Long chairId);
    
       // Find sessions by chair with pagination (excluding soft-deleted)
       Page<Session> findByChairIdAndDeletedFalse(Long chairId, Pageable pageable);

       // Check if active sessions reference a given room
       boolean existsByRoomIdAndDeletedFalse(Long roomId);

       // Check if any session (including soft-deleted) references a room
       boolean existsByRoomId(Long roomId);

       // Fetch all sessions by room (including soft-deleted)
       List<Session> findByRoomId(Long roomId);

       // Find all active (not deleted) sessions
       List<Session> findByDeletedFalse();

       // Paginated active sessions
       Page<Session> findByDeletedFalse(Pageable pageable);
    
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

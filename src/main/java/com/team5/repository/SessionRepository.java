package com.team5.repository;

import com.team5.entity.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    
    // Override default methods to exclude deleted sessions
    @Query("SELECT s FROM Session s WHERE s.isDeleted = false")
    List<Session> findAll();
    
    @Query("SELECT s FROM Session s WHERE s.id = :id AND s.isDeleted = false")
    java.util.Optional<Session> findById(@Param("id") Long id);
    
    @Query("SELECT s FROM Session s WHERE s.status = :status AND s.isDeleted = false")
    List<Session> findByStatus(@Param("status") Session.SessionStatus status);
    
    @Query("SELECT s FROM Session s WHERE s.chair.id = :chairId AND s.isDeleted = false")
    List<Session> findByChairId(@Param("chairId") Long chairId);
    
    @Query("SELECT s FROM Session s WHERE s.title = :title AND s.startTime = :startTime AND s.isDeleted = false")
    boolean existsByTitleAndStartTime(@Param("title") String title, @Param("startTime") LocalDateTime startTime);
    
    @Query("SELECT s FROM Session s WHERE s.startTime > :dateTime AND s.isDeleted = false")
    List<Session> findByStartTimeAfter(@Param("dateTime") LocalDateTime dateTime);
    
    // Pagination methods
    @Query("SELECT s FROM Session s WHERE s.status = :status AND s.isDeleted = false")
    Page<Session> findByStatus(@Param("status") Session.SessionStatus status, Pageable pageable);
    
    @Query("SELECT s FROM Session s WHERE s.chair.id = :chairId AND s.isDeleted = false")
    Page<Session> findByChairId(@Param("chairId") Long chairId, Pageable pageable);
    
    @Query("SELECT s FROM Session s WHERE s.title LIKE %:title% AND s.isDeleted = false")
    Page<Session> findByTitleContaining(@Param("title") String title, Pageable pageable);
    
    @Query("SELECT s FROM Session s WHERE s.startTime BETWEEN :startDate AND :endDate AND s.isDeleted = false")
    Page<Session> findByStartTimeBetween(@Param("startDate") LocalDateTime startDate, 
                                       @Param("endDate") LocalDateTime endDate, 
                                       Pageable pageable);
    
    @Query("SELECT s FROM Session s WHERE s.chair.id = :chairId AND s.status = :status AND s.isDeleted = false")
    Page<Session> findByChairIdAndStatus(@Param("chairId") Long chairId, 
                                         @Param("status") Session.SessionStatus status, 
                                         Pageable pageable);
    
    // Soft delete specific methods
    @Query("SELECT s FROM Session s WHERE s.isDeleted = true")
    List<Session> findDeletedSessions();
    
    @Query("SELECT s FROM Session s WHERE s.isDeleted = true")
    Page<Session> findDeletedSessions(Pageable pageable);
}

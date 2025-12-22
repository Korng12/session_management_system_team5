package com.team5.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.team5.demo.entities.Paper;
import com.team5.demo.entities.Paper.PaperStatus; // <--- FIXED IMPORT

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaperRepository extends JpaRepository<Paper, Long> {
    
    // Uses the nested PaperStatus enum
    List<Paper> findByStatus(PaperStatus status);
    
    List<Paper> findBySessionId(Long sessionId);
    
    List<Paper> findByAuthorNamesContaining(String authorName);
    
    List<Paper> findByTitleContainingIgnoreCase(String title);
    
    @Query("SELECT p FROM Paper p WHERE p.submissionDate BETWEEN :startDate AND :endDate")
    List<Paper> findBySubmissionDateBetween(@Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    boolean existsByTitleAndAuthorNames(String title, String authorNames);
    
    @Query("SELECT COUNT(p) FROM Paper p WHERE p.status = :status")
    long countByStatus(@Param("status") PaperStatus status);
    
    @Query("SELECT p FROM Paper p WHERE p.session IS NULL AND p.status = :status")
    List<Paper> findUnassignedPapersByStatus(@Param("status") PaperStatus status);
}
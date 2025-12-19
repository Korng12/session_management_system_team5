// src/main/java/com/team5/repository/SessionRepository.java
package com.team5.demo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SessionRepository extends JpaRepository<Session, Long> {
    List<Session> findByStatus(Session.SessionStatus status);
    List<Session> findByChairId(Long chairId);
    boolean existsByTitleAndStartTime(String title, LocalDateTime startTime);
}
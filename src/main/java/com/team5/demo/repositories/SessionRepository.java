package com.team5.demo.repositories;

import com.team5.demo.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    // Find all sessions for a specific conference
    List<Session> findByConferenceId(Integer conferenceId);

    // Find all sessions for a specific room
    List<Session> findByRoomId(Integer roomId);

    // Find all sessions with a specific chair
    List<Session> findByChairId(Integer chairId);
}

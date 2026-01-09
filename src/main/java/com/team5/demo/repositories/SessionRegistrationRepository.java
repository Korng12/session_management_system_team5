package com.team5.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionRegistration;
import com.team5.demo.entities.User;

public interface SessionRegistrationRepository extends JpaRepository <SessionRegistration,Long> {
    List <SessionRegistration> findByParticipant(User participant);
    boolean existsByParticipantAndSession(User participant,Session session) ;
    
} 

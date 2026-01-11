package com.team5.demo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionRegistration;
import com.team5.demo.entities.User;

public interface SessionRegistrationRepository extends JpaRepository <SessionRegistration,Long> {
    List <SessionRegistration> findByParticipant(User participant);
    boolean existsByParticipantAndSession(User participant,Session session) ;
    List<SessionRegistration> findBySession(Session session);
    Optional<SessionRegistration>  findByParticipantAndSession(User user, Session session);
       @Query("""
    select sr.session.id
    from SessionRegistration sr
    where sr.participant = :user
      and sr.session.conference.id = :conferenceId
    """)
    List<Long> findRegisteredSessionIds(
        @Param("user") User user,
        @Param("conferenceId") Long conferenceId
);


} 

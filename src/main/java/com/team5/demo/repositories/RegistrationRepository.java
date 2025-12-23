package com.team5.demo.repositories;

import com.team5.demo.entities.Registration;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    // Keep existing methods for backward compatibility
    @Query("SELECT r FROM Registration r WHERE r.participant.id = :participantId AND r.conference.id = :conferenceId")
    Optional<Registration> findByParticipantIdAndConferenceId(@Param("participantId") Long participantId, 
                                                           @Param("conferenceId") Long conferenceId);
    
    @Query("SELECT r FROM Registration r WHERE r.participant.id = :participantId")
    List<Registration> findByParticipantId(@Param("participantId") Long participantId);
    
    @Query("SELECT r FROM Registration r WHERE r.conference.id = :conferenceId")
    List<Registration> findByConferenceId(@Param("conferenceId") Long conferenceId);
    
    @Query("SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END FROM Registration r WHERE r.participant.id = :participantId AND r.conference.id = :conferenceId")
    boolean existsByParticipantIdAndConferenceId(@Param("participantId") Long participantId, 
                                              @Param("conferenceId") Long conferenceId);
    
    // New methods using entity relationships
    Optional<Registration> findByParticipantAndConference(User participant, Session conference);
    List<Registration> findByParticipant(User participant);
    List<Registration> findByConference(Session conference);
    boolean existsByParticipantAndConference(User participant, Session conference);
}
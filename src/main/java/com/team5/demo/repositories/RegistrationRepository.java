package com.team5.demo.repositories;

import com.team5.demo.entities.Registration;
import com.team5.demo.entities.User;
import com.team5.demo.enums.RegistrationStatus;
import com.team5.demo.entities.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    
    
    Optional<Registration> findByParticipantAndConference(User participant, Conference conference);
    
    List<Registration> findByParticipant(User participant);
    
    List<Registration> findByConference(Conference conference);
    
    // List<Registration> findByParticipantAndStatus(User participant, String status);
    List<Registration> findByParticipantAndStatus(User participant, RegistrationStatus status);

    
    // List<Registration> findByConferenceAndStatus(Conference conference, String status);
    List<Registration> findByConferenceAndStatus(Conference conference, RegistrationStatus status);

    
    long countByConference(Conference conference);
    
    // boolean existsByParticipantIdAndConferenceId(Long userId, Long conferenceId);
    // boolean existsByParticipantAndConference(User participant, Conference conference);
    boolean existsByParticipantAndConferenceAndStatus(
        User participant,
        Conference conference,
        RegistrationStatus status
    );

    long countByParticipant(User participant);
    
    // List<Registration> findByStatus(String status);
    List<Registration> findByStatus(RegistrationStatus status);

}
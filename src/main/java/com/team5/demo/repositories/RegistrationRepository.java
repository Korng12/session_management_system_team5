package com.team5.demo.repositories;

import com.team5.demo.entities.Registration; 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByParticipantIdAndConferenceId(Long participantId, Long conferenceId);
    List<Registration> findByParticipantId(Long participantId);
    List<Registration> findByConferenceId(Long conferenceId);
    boolean existsByParticipantIdAndConferenceId(Long participantId, Long conferenceId);
}
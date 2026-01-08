package com.team5.demo.services;

import com.team5.demo.entities.*;
import com.team5.demo.enums.RegistrationStatus;
import com.team5.demo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

        private final RegistrationRepository registrationRepository;
        private final UserRepository userRepository;
        private final ConferenceRepository conferenceRepository;
        private final com.team5.demo.repositories.SessionRegistrationRepository sessionRegistrationRepository;

        @Transactional

    /**
     * Registers a user for a conference if not already registered.
     * @param userId the user ID
     * @param conferenceId the conference ID
     * @return the created Registration
     * @throws RuntimeException if user or conference not found, or already registered
     */
    public Registration registerForConference(Long userId, Long conferenceId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));

        if (registrationRepository.findByParticipantAndConference(user, conference).isPresent()) {
            throw new RuntimeException("User is already registered for this conference");
        }

        Registration registration = new Registration();
        registration.setParticipant(user);
        registration.setConference(conference);
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setStatus(RegistrationStatus.CONFIRMED);
        return registrationRepository.save(registration);
    }
    
    public Registration getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + registrationId));
    }
    
    public List<Registration> getUserRegistrations(Long userId) {
        // Find user first (REQUIRED for JPA)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        //Find registrations by user entity
        return registrationRepository.findByParticipant(user);
    }
    
        /**
         * Cancels a registration by setting its status to CANCELED.
         * @param registrationId the registration ID
         */
        @Transactional
        public void cancelRegistration(Long registrationId) {
            Registration registration = getRegistration(registrationId);

            // Disallow cancelling registration for conferences that have already ended
            Conference conf = registration.getConference();
            if (conf.getEndDate() != null && !conf.getEndDate().isAfter(java.time.LocalDate.now())) {
                throw new IllegalStateException("Cannot cancel registration for a conference that has already completed");
            }

            // Prevent cancelling while the user still has session registrations in this conference
            var participant = registration.getParticipant();
            var registeredSessionIds = sessionRegistrationRepository.findRegisteredSessionIds(participant, conf.getId());
            if (registeredSessionIds != null && !registeredSessionIds.isEmpty()) {
                throw new IllegalStateException("Cannot cancel conference registration while registered for sessions. Please cancel session registrations first.");
            }

            registration.setStatus(RegistrationStatus.CANCELED);
            registrationRepository.save(registration);
        }
    
    // Additional useful methods
    public List<Registration> getConferenceRegistrations(Long conferenceId) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));
        
        return registrationRepository.findByConference(conference);
    }
    
    public long countRegistrationsForConference(Long conferenceId) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));
        
        return registrationRepository.countByConference(conference);
    }
    
    public boolean isUserRegisteredForConference(Long userId, Long conferenceId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));
        
        return registrationRepository.findByParticipantAndConference(user, conference)
                .isPresent();
    }
}
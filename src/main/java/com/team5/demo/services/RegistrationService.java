package com.team5.demo.services;

import com.team5.demo.entities.*;
import com.team5.demo.enums.RegistrationStatus;
import com.team5.demo.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegistrationService {
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ConferenceRepository conferenceRepository;
    private final com.team5.demo.repositories.SessionRegistrationRepository sessionRegistrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository, UserRepository userRepository, ConferenceRepository conferenceRepository, com.team5.demo.repositories.SessionRegistrationRepository sessionRegistrationRepository) {
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
        this.conferenceRepository = conferenceRepository;
        this.sessionRegistrationRepository = sessionRegistrationRepository;
    }

    @Transactional
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

    @Transactional
    public Registration registerForConference(String email, Long conferenceId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return registerForConference(user.getId(), conferenceId);
    }

    public Registration getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + registrationId));
    }

    public List<Registration> getUserRegistrations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
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

    @Transactional
    public void cancelRegistrationByEmailAndConferenceId(String email, Long conferenceId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Conference conf = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        Registration registration = registrationRepository.findByParticipantAndConference(user, conf)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        registration.setStatus(RegistrationStatus.CANCELED);
        registrationRepository.save(registration);
    }

    public List<Registration> getMyConferences(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return registrationRepository.findByParticipant(user);
    }

    public List<Registration> getConferenceRegistrations(Long conferenceId) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));
        return registrationRepository.findByConference(conference);
    }
}
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

    @Transactional
    public Registration registerForConference(String email, Long conferenceId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return registerForConference(user.getId(), conferenceId);
    }

    public Registration getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found with id: " + registrationId));
    }

    public List<Registration> getUserRegistrations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return registrationRepository.findByParticipant(user);
    }
<<<<<<< HEAD
    
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
=======

    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = getRegistration(registrationId);
        registration.setStatus(RegistrationStatus.CANCELED);
        registrationRepository.save(registration);
>>>>>>> 4e95be6 (mege mesa branch with main)
    }

    @Transactional
    public void cancelRegistrationByEmailAndConferenceId(String email, Long conferenceId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        Conference conf = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        Registration registration = registrationRepository.findByParticipantAndConference(user, conf)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        registration.setStatus(RegistrationStatus.CANCELED);
        registrationRepository.save(registration);
    }

    public List<Registration> getMyConferences(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        return registrationRepository.findByParticipant(user);
    }

    public List<Registration> getConferenceRegistrations(Long conferenceId) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));
        return registrationRepository.findByConference(conference);
    }
}


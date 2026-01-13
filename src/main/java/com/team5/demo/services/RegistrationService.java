package com.team5.demo.services;

import com.team5.demo.entities.Conference;
import com.team5.demo.entities.Registration;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.ConferenceRepository;
import com.team5.demo.repositories.RegistrationRepository;
import com.team5.demo.repositories.UserRepository;
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

        @Transactional
        public Registration registerForConference(String email, Long conferenceId) {

                User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

                 Conference conf = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        // Disallow registration for conferences that have already ended
        if (conf.getEndDate() != null && conf.getEndDate().isBefore(java.time.LocalDate.now())) {
            throw new IllegalStateException("Cannot register for a conference that has already completed");
        }

        Optional<Registration> regOpt=registrationRepository.findByParticipantAndConference(user,conf);

        if(regOpt.isPresent()){
                Registration exitstingReg =regOpt.get();
                if(exitstingReg.getStatus() == RegistrationStatus.CONFIRMED){
                        throw new RuntimeException("Already registered");
                }
                exitstingReg.setStatus(RegistrationStatus.CONFIRMED);
                exitstingReg.setRegisteredAt(LocalDateTime.now());
                return exitstingReg;
        }

                Registration reg = new Registration();
                reg.setParticipant(user);
                reg.setConference(conf);
                reg.setRegisteredAt(LocalDateTime.now());
                //     reg.setStatus("CONFIRMED");
                        reg.setStatus(RegistrationStatus.CONFIRMED);

                return registrationRepository.save(reg);
        }

        public Registration getRegistration(Long registrationId) {
                return registrationRepository.findById(registrationId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Registration not found with id: " + registrationId));
        }

        public List<Registration> getUserRegistrations(Long userId) {
                // Find user first (REQUIRED for JPA)
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

                // Find registrations by user entity
                return registrationRepository.findByParticipant(user);
        }

        public List<Registration> getMyConferences(String email) {

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                return registrationRepository.findByParticipant(user);
        }

        @Transactional
        public void cancelRegistration(Long registrationId) {
                Registration registration = getRegistration(registrationId);

                // Disallow cancelling registration for conferences that have already ended
                Conference conf = registration.getConference();
                if (conf.getEndDate() != null && !conf.getEndDate().isAfter(java.time.LocalDate.now())) {
                    throw new IllegalStateException("Cannot cancel registration for a conference that has already completed");
                }


                // registration.setStatus("CANCELLED");
                registration.setStatus(RegistrationStatus.CANCELED);
                registrationRepository.save(registration);
        }

        // Additional useful methods
        public List<Registration> getConferenceRegistrations(Long conferenceId) {
                Conference conference = conferenceRepository.findById(conferenceId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Conference not found with id: " + conferenceId));

                return registrationRepository.findByConference(conference);
        }

        public long countRegistrationsForConference(Long conferenceId) {
                Conference conference = conferenceRepository.findById(conferenceId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Conference not found with id: " + conferenceId));

                return registrationRepository.countByConference(conference);
        }

        public boolean isUserRegisteredForConference(Long userId, Long conferenceId) {
                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

                Conference conference = conferenceRepository.findById(conferenceId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Conference not found with id: " + conferenceId));

                return registrationRepository.findByParticipantAndConference(user, conference)
                                .isPresent();
        }

        public java.util.Optional<Registration> getRegistrationForUserAndConference(String email, Long conferenceId) {
                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                Conference conference = conferenceRepository.findById(conferenceId)
                                .orElseThrow(() -> new RuntimeException("Conference not found"));

                // Only consider an active registration if its status is CONFIRMED
                return registrationRepository.findByParticipantAndConference(user, conference)
                                .filter(reg->RegistrationStatus.CONFIRMED == reg.getStatus());
                                // .filter(reg -> "CONFIRMED".equalsIgnoreCase(reg.getStatus()));
        }
        @Transactional(readOnly = true)
        public boolean isRegisteredForConference(String email, Long conferenceId) {

                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new RuntimeException("User not found"));

                Conference conference = conferenceRepository.findById(conferenceId)
                        .orElseThrow(() -> new RuntimeException("Conference not found"));

                return registrationRepository.existsByParticipantAndConferenceAndStatus(
                        user,
                        conference,
                        RegistrationStatus.CONFIRMED
                );
        }
        public List<Registration> getAllRegistrations() {
             return registrationRepository.findAll();
        }
        public Long getTotalRegistrations() {
             return registrationRepository.count();
        }
                public List<Registration> searchByParticipant(String keyword) {
        return registrationRepository
                .findByParticipant_EmailContainingIgnoreCase(keyword);
        }

        public List<Registration> getMyRegistrationsByEmail(String email) {
            return registrationRepository.findMyRegistrationsByEmail(email);
        }
}

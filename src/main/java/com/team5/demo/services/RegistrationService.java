package com.team5.demo.services;

import com.team5.demo.entities.*;
import com.team5.demo.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;
    private final ConferenceRepository conferenceRepository;

    // ================= USER REGISTRATION =================
    @Transactional
    public Registration registerForConference(String email, Long conferenceId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Conference conf = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        if (registrationRepository.existsByParticipantAndConference(user, conf)) {
            throw new RuntimeException("Already registered");
        }

        Registration reg = new Registration();
        reg.setParticipant(user);
        reg.setConference(conf);
        reg.setRegisteredAt(LocalDateTime.now());
        reg.setStatus("CONFIRMED");

        return registrationRepository.save(reg);
    }

    // ================= ADMIN REGISTRATION (NEW) =================
    @Transactional
    public Registration adminRegisterUser(Long userId, Long conferenceId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId));

        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() ->
                        new RuntimeException("Conference not found with id: " + conferenceId));

        if (registrationRepository.existsByParticipantAndConference(user, conference)) {
            throw new RuntimeException("User already registered for this conference");
        }

        Registration reg = new Registration();
        reg.setParticipant(user);
        reg.setConference(conference);
        reg.setRegisteredAt(LocalDateTime.now());
        reg.setStatus("CONFIRMED");

        return registrationRepository.save(reg);
    }

    // ================= READ =================
    public Registration getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() ->
                        new RuntimeException("Registration not found with id: " + registrationId));
    }

    public List<Registration> getUserRegistrations(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId));

        return registrationRepository.findByParticipant(user);
    }

    public List<Registration> getMyConferences(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return registrationRepository.findByParticipant(user);
    }

    // ================= UPDATE =================
    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = getRegistration(registrationId);
        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);
    }

    // ================= CONFERENCE HELPERS =================
    public List<Registration> getConferenceRegistrations(Long conferenceId) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() ->
                        new RuntimeException("Conference not found with id: " + conferenceId));

        return registrationRepository.findByConference(conference);
    }

    public long countRegistrationsForConference(Long conferenceId) {
        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() ->
                        new RuntimeException("Conference not found with id: " + conferenceId));

        return registrationRepository.countByConference(conference);
    }

    public boolean isUserRegisteredForConference(Long userId, Long conferenceId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found with id: " + userId));

        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() ->
                        new RuntimeException("Conference not found with id: " + conferenceId));

        return registrationRepository
                .findByParticipantAndConference(user, conference)
                .isPresent();
    }
}

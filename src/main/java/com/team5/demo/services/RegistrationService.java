package com.team5.demo.services;

import com.team5.demo.entities.Conference;
import com.team5.demo.entities.Registration;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.ConferenceRepository;
import com.team5.demo.repositories.RegistrationRepository;
import com.team5.demo.repositories.UserRepository;
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

    /* ===================== REGISTER ===================== */

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

    public Registration getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException(
                        "Registration not found with id: " + registrationId));
    }

    /* ===================== CANCEL ===================== */

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

        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);
    }

    @Transactional
    public void cancelRegistrationByEmailAndConferenceId(String email, Long conferenceId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Conference conf = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
        Registration registration = registrationRepository.findByParticipantAndConference(user, conf)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);
    }

    public long getTotalRegistrations() {
        return registrationRepository.count();
    }

    // ✅ Admin sees CONFIRMED registrations with relations
    // ✅ FOR USER: MY CONFERENCES
    @Transactional(readOnly = true)
    public List<Registration> getMyRegistrationsByEmail(String email) {

        List<Registration> list = registrationRepository.findMyRegistrationsByEmail(email);

        System.out.println("🔥 DB registrations = " + list.size());
        return list;
    }

    @Transactional(readOnly = true)
    public List<Registration> getAllRegistrations() {
        return registrationRepository.findAllWithRelations();
    }

    // --------
    // @Transactional(readOnly = true)
    // public List<Registration> getMyRegistrationsByEmail(String email) {
    // List<Registration> list =
    // registrationRepository.findMyRegistrationsByEmail(email);

    // System.out.println("🔥 DB registrations = " + list.size());
    // return list;
    // }

    // ✅ Admin search (CONFIRMED only)
    @Transactional(readOnly = true)
    public List<Registration> searchByParticipant(String keyword) {
        return registrationRepository.searchConfirmedByParticipant(keyword);
    }
    
    public List<Registration> getMyConferences(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
        
        return registrationRepository.findByParticipant(user);
    }
}

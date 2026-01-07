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
//     public Registration registerForConference(Long userId, Long conferenceId) {
//         // Find User and Conference entities (REQUIRED for JPA)
//         User user = userRepository.findById(userId)
//                 .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
//         Conference conference = conferenceRepository.findById(conferenceId)
//                 .orElseThrow(() -> new RuntimeException("Conference not found with id: " + conferenceId));
        
//         // Check if already registered using JPA entities
//         boolean alreadyRegistered = registrationRepository
//                 .findByParticipantAndConference(user, conference)
//                 .isPresent();
        
//         if (alreadyRegistered) {
//             throw new RuntimeException("User is already registered for this conference");
//         }
        
//         // Create registration with JPA entities
//         Registration registration = new Registration();
//         registration.setParticipant(user);        // Set User entity (not ID)
//         registration.setConference(conference);   // Set Conference entity (not ID)
//         registration.setRegisteredAt(LocalDateTime.now());
//         registration.setStatus("CONFIRMED");
        
//         return registrationRepository.save(registration);
//     }
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
                .orElseThrow(() ->
                        new RuntimeException(
                                "Registration not found with id: " + registrationId));
    }

    /* ===================== CANCEL ===================== */

    
    public List<Registration> getUserRegistrations(Long userId) {
        // Find user first (REQUIRED for JPA)
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        //Find registrations by user entity
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
}

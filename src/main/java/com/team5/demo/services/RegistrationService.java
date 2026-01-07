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
    public Registration registerForConference(Long userId, Long conferenceId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Conference conference = conferenceRepository.findById(conferenceId)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        // ✅ Prevent duplicate registration
        if (registrationRepository
                .existsByParticipant_IdAndConference_Id(userId, conferenceId)) {
            throw new RuntimeException("User already registered for this conference");
        }

        Registration registration = new Registration(user, conference);
        registration.setStatus("CONFIRMED");
        registration.setRegisteredAt(LocalDateTime.now());

        return registrationRepository.save(registration);
    }

    /* ===================== USER ===================== */

    // ✅ User sees ONLY confirmed registrations
    @Transactional(readOnly = true)
    public List<Registration> getUserRegistrations(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return registrationRepository
                .findByParticipantAndStatus(user, "CONFIRMED");
    }

    /* ===================== ADMIN ===================== */

    // ✅ Admin sees CONFIRMED registrations with relations
    @Transactional(readOnly = true)
    public List<Registration> getAllRegistrations() {

        List<Registration> list =
                registrationRepository.findByStatusWithRelations("CONFIRMED");

        System.out.println("🔥 CONFIRMED REGISTRATIONS = " + list.size());
        return list;
    }

    // ✅ Admin search (CONFIRMED only)
    @Transactional(readOnly = true)
    public List<Registration> searchByParticipant(String keyword) {
        return registrationRepository.searchConfirmedByParticipant(keyword);
    }

    /* ===================== SINGLE REGISTRATION ===================== */

    @Transactional(readOnly = true)
    public Registration getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Registration not found with id: " + registrationId));
    }

    /* ===================== CANCEL ===================== */

    @Transactional
    public void cancelRegistration(Long registrationId) {

        Registration registration = getRegistration(registrationId);

        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);
    }
}

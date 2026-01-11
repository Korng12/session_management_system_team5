package com.team5.demo.services;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team5.demo.entities.Registration;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionRegistration;
import com.team5.demo.entities.User;
import com.team5.demo.enums.RegistrationStatus;
import com.team5.demo.repositories.RegistrationRepository;
import com.team5.demo.repositories.SessionRegistrationRepository;
import com.team5.demo.repositories.SessionRepository;
import com.team5.demo.repositories.UserRepository;

@Service
@Transactional
public class SessionRegistrationService {

    private final UserRepository userRepo;
    private final SessionRepository sessionRepo;
    private final RegistrationRepository registrationRepo;
    private final SessionRegistrationRepository sessionRegRepo;

    public SessionRegistrationService(
            UserRepository userRepo,
            SessionRepository sessionRepo,
            RegistrationRepository registrationRepo,
            SessionRegistrationRepository sessionRegRepo) {

        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.registrationRepo = registrationRepo;
        this.sessionRegRepo = sessionRegRepo;
    }

    public void registerForSession(Long sessionId, String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        // 🔒 Must be registered for conference
        Registration confReg =
            registrationRepo
                .findByParticipantAndConference(user, session.getConference())
                .orElseThrow(() ->
                    new IllegalStateException("Register for conference first"));

        if (confReg.getStatus() != RegistrationStatus.CONFIRMED) {
            throw new IllegalStateException("Conference registration is not active");
        }

        // 🔒 Prevent duplicates
        if (sessionRegRepo.existsByParticipantAndSession(user, session)) {
            throw new IllegalStateException("Already registered for session");
        }

        SessionRegistration sr = new SessionRegistration();
        sr.setParticipant(user);
        sr.setSession(session);

        sessionRegRepo.save(sr);
    }

    public void cancelSessionRegistration(Long sessionId, String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow();

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow();

        SessionRegistration reg =
            sessionRegRepo.findByParticipantAndSession(user, session)
                .orElseThrow(() ->
                    new IllegalStateException("Not registered for session"));

        sessionRegRepo.delete(reg);
    }
@Transactional(readOnly = true)
public Set<Long> getRegisteredSessionIds(String email, Long conferenceId) {

    User user = userRepo.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    return sessionRegRepo
            .findRegisteredSessionIds(user, conferenceId)
            .stream()
            .collect(Collectors.toSet());
}


}

package com.team5.demo.service;

import com.team5.demo.model.Registration;  // FIXED: Changed from com.conference
import com.team5.demo.repository.RegistrationRepository;  // FIXED: Changed from com.conference
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    
    private final RegistrationRepository registrationRepository;
    
    @Transactional
    public Registration registerForConference(Long userId, Long conferenceId) {
        // Check if already registered
        if (registrationRepository.existsByParticipantIdAndConferenceId(userId, conferenceId)) {
            throw new RuntimeException("User is already registered for this conference");  // Simplified
        }
        
        Registration registration = new Registration();
        registration.setParticipantId(userId);  // Simplified - just set ID
        registration.setConferenceId(conferenceId);
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setStatus("CONFIRMED");
        
        return registrationRepository.save(registration);
    }
    
    public Registration getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));  // Simplified
    }
    
    public List<Registration> getUserRegistrations(Long userId) {
        return registrationRepository.findByParticipantId(userId);
    }
    
    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = getRegistration(registrationId);
        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);
    }
}
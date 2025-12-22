package com.team5.demo.service;

import com.team5.demo.dto.RegistrationDTO;
import com.team5.demo.repositories.RegistrationRepository;  
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import com.team5.demo.entities.Registration;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    
    private final RegistrationRepository registrationRepository;
    
    @Transactional
    public RegistrationDTO registerForConference(Long userId, Long conferenceId) {
        // Check if already registered
        if (registrationRepository.existsByParticipantIdAndConferenceId(userId, conferenceId)) {
            throw new RuntimeException("User is already registered for this conference");
        }
        
        Registration registration = new Registration();
        registration.setParticipantId(userId);
        registration.setConferenceId(conferenceId);
        registration.setRegisteredAt(LocalDateTime.now());
        registration.setStatus("CONFIRMED");
        
        Registration savedRegistration = registrationRepository.save(registration);
        return mapToDTO(savedRegistration);
    }
    
    public RegistrationDTO getRegistration(Long registrationId) {
        return registrationRepository.findById(registrationId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }
    
    public List<RegistrationDTO> getUserRegistrations(Long userId) {
        return registrationRepository.findByParticipantId(userId).stream()
                .map(this::mapToDTO)
                .collect(java.util.stream.Collectors.toList());
    }
    
    @Transactional
    public void cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
        registration.setStatus("CANCELLED");
        registrationRepository.save(registration);
    }
    
    private RegistrationDTO mapToDTO(Registration registration) {
        RegistrationDTO dto = new RegistrationDTO();
        dto.setId(registration.getId());
        dto.setSessionId(registration.getConferenceId());
        dto.setUserId(registration.getParticipantId());
        dto.setRegistrationTime(registration.getRegisteredAt());
        dto.setStatus(registration.getStatus());
        return dto;
    }
}
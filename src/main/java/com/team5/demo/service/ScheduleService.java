package com.team5.demo.service;

import com.team5.demo.dto.ScheduleDTO;  // FIXED: Changed from com.conference
import com.team5.demo.model.Session;  // FIXED: Changed from com.conference
import com.team5.demo.repository.SessionRepository;  // FIXED: Changed from com.conference
import com.team5.demo.repository.RegistrationRepository;  // FIXED: Changed from com.conference
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    
    private final SessionRepository sessionRepository;
    private final RegistrationRepository registrationRepository;
    
    public List<ScheduleDTO> getUserSchedule(Long userId) {
        // Get all sessions the user is attending
        List<Session> sessions = sessionRepository.findSessionsByParticipantId(userId);
        
        return sessions.stream()
                .map(this::convertToScheduleDTO)
                .collect(Collectors.toList());
    }
    
    public boolean isUserRegisteredForConference(Long userId, Long conferenceId) {
        return registrationRepository.existsByParticipantIdAndConferenceId(userId, conferenceId);
    }
    
    private ScheduleDTO convertToScheduleDTO(Session session) {
        ScheduleDTO dto = new ScheduleDTO();
        dto.setSessionId(session.getId());
        dto.setTitle(session.getTitle());
        dto.setLocation(session.getLocation());
        dto.setStartTime(session.getStartTime());
        dto.setEndTime(session.getEndTime());
        dto.setRoomName(session.getRoom() != null ? session.getRoom().getName() : "TBD");
        return dto;
    }
}
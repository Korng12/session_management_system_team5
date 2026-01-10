package com.team5.demo.services;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team5.demo.dto.UserScheduleDto;
import com.team5.demo.entities.AttendanceStatus;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionRegistration;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.SessionAttendanceRepository;
import com.team5.demo.repositories.SessionRegistrationRepository;
import com.team5.demo.repositories.UserRepository;



// import com.team5.demo.dto.ScheduleDTO;  // FIXED: Changed from com.conference
// import com.team5.demo.entities.Session;
// import com.team5.demo.repositories.RegistrationRepository;
// import com.team5.demo.repositories.SessionRepository;

// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Service;
// import java.util.List;
// import java.util.stream.Collectors;

// @Service
// @RequiredArgsConstructor
// public class ScheduleService {
    
//     private final SessionRepository sessionRepository;
//     private final RegistrationRepository registrationRepository;
    
//     public List<ScheduleDTO> getUserSchedule(Long userId) {
//         // Get all sessions the user is attending
//         List<Session> sessions = sessionRepository.findSessionsByParticipantId(userId);
        
//         return sessions.stream()
//                 .map(this::convertToScheduleDTO)
//                 .collect(Collectors.toList());
//     }
    
//     public boolean isUserRegisteredForConference(Long userId, Long conferenceId) {
//         return registrationRepository.existsByParticipantIdAndConferenceId(userId, conferenceId);
//     }
    
//     private ScheduleDTO convertToScheduleDTO(Session session) {
//         ScheduleDTO dto = new ScheduleDTO();
//         dto.setSessionId(session.getId());
//         dto.setTitle(session.getTitle());
//         dto.setLocation(session.getLocation());
//         dto.setStartTime(session.getStartTime());
//         dto.setEndTime(session.getEndTime());
//         dto.setRoomName(session.getRoom() != null ? session.getRoom().getName() : "TBD");
//         return dto;
//     }
// }

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    private final UserRepository userRepo;
    private final SessionRegistrationRepository sessionRegRepo;
    private final SessionAttendanceRepository attendanceRepo;

    public ScheduleService(
            UserRepository userRepo,
            SessionRegistrationRepository sessionRegRepo,
            SessionAttendanceRepository attendanceRepo) {
        this.userRepo = userRepo;
        this.sessionRegRepo = sessionRegRepo;
        this.attendanceRepo = attendanceRepo;
    }

    public List<UserScheduleDto> getUserSchedule(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<SessionRegistration> registrations = sessionRegRepo.findByParticipant(user);

        return registrations.stream().map(reg -> {

            Session s = reg.getSession();

            AttendanceStatus attendance =
                    attendanceRepo
                        .findByParticipantAndSession(user, s)
                        .map(SessionAttendance::getStatus)
                        .orElse(null);

            return new UserScheduleDto(
  
                    s.getTitle(),
                    s.getStartTime().toLocalDate(),
                    s.getStartTime().toLocalTime(),
                    s.getEndTime().toLocalTime(),
                    s.getStatus(),
                    attendance
            );

        }).toList();
    }
}

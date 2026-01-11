package com.team5.demo.services;

import java.util.List;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team5.demo.dto.MyUpcomingSessionDto;
import com.team5.demo.dto.UserScheduleDto;
import com.team5.demo.entities.AttendanceStatus;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionRegistration;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.SessionAttendanceRepository;
import com.team5.demo.repositories.SessionRegistrationRepository;
import com.team5.demo.repositories.UserRepository;


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
    public List<MyUpcomingSessionDto> getMyUpcomingSessions(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return sessionRegRepo.findByParticipant(user)
                .stream()
                .map(reg -> {

                    Session s = reg.getSession();

                    return new MyUpcomingSessionDto(
                            s.getId(),
                            s.getTitle(),
                            s.getConference().getTitle(),
                            s.getStartTime(),
                            s.getEndTime(),
                            s.getStatus()
                    );
                })
                .toList();
    }
    
}

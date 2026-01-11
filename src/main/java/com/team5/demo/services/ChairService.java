package com.team5.demo.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.team5.demo.dto.AttendanceDto;
import com.team5.demo.entities.AttendanceStatus;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionRegistration;
import com.team5.demo.entities.SessionStatus;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.SessionAttendanceRepository;
import com.team5.demo.repositories.SessionRegistrationRepository;
import com.team5.demo.repositories.SessionRepository;
import com.team5.demo.repositories.UserRepository;

@Service
@Transactional
public class ChairService {

    private final UserRepository userRepo;
    private final SessionRepository sessionRepo;
    private final SessionRegistrationRepository sessionRegRepo;
    private final SessionAttendanceRepository attendanceRepo;

    public ChairService(UserRepository userRepo,
                        SessionRepository sessionRepo,
                        SessionRegistrationRepository sessionRegRepo,
                        SessionAttendanceRepository attendanceRepo) {
        this.userRepo = userRepo;
        this.sessionRepo = sessionRepo;
        this.sessionRegRepo = sessionRegRepo;
        this.attendanceRepo = attendanceRepo;
    }

    /**
     * Refresh a session's status based on current time:
     * - before startTime  -> SCHEDULED
     * - between start/end -> ONGOING
     * - after endTime     -> COMPLETED
     *
     * Do NOT override CANCELLED.
     *
     * @return true if status changed, else false
     */
    private boolean refreshStatusFromTime(Session session) {
        if (session.getStatus() == SessionStatus.CANCELLED) {
            return false; // don't override cancelled sessions
        }

        LocalDateTime now = LocalDateTime.now();

        SessionStatus newStatus;
        if (now.isBefore(session.getStartTime())) {
            newStatus = SessionStatus.SCHEDULED;
        } else if (!now.isAfter(session.getEndTime())) {
            // now >= startTime AND now <= endTime
            newStatus = SessionStatus.ONGOING;
        } else {
            newStatus = SessionStatus.COMPLETED;
        }

        if (session.getStatus() != newStatus) {
            session.setStatus(newStatus);
            return true;
        }
        return false;
    }

    // 1️⃣ Sessions chaired by current user
    // NOTE: removed readOnly because we may update statuses here
    public List<Session> getChairedSessions(String email) {

        User chair = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Session> sessions = sessionRepo.findByChair(chair);

        // Refresh each session status based on time so UI becomes correct (ONGOING when it should)
        for (Session s : sessions) {
            if (refreshStatusFromTime(s)) {
                sessionRepo.save(s);
            }
        }

        return sessions;
    }

    // 2️⃣ Registered attendees for a session
    @Transactional(readOnly = true)
    public List<User> getRegisteredAttendees(Long sessionId, String email) {

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        User chair = userRepo.findByEmail(email).orElseThrow();

        if (!session.getChair().getId().equals(chair.getId())) {
            throw new AccessDeniedException("Not session chair");
        }

        return sessionRegRepo.findBySession(session)
                .stream()
                .map(SessionRegistration::getParticipant)
                .toList();
    }

    // 3️⃣ Mark attendance
    public void markAttendance(Long sessionId,
                               Long participantId,
                               AttendanceStatus status,
                               String chairEmail) {

        Session session = sessionRepo.findById(sessionId)
                .orElseThrow();

        User chair = userRepo.findByEmail(chairEmail)
                .orElseThrow();

        if (!session.getChair().getId().equals(chair.getId())) {
            throw new AccessDeniedException("Not session chair");
        }

        //  Refresh status before enforcing "ONGOING"
        if (refreshStatusFromTime(session)) {
            sessionRepo.save(session);
        }

        // Only allow marking attendance while session is ongoing
        if (session.getStatus() != com.team5.demo.entities.SessionStatus.ONGOING) {
            throw new IllegalStateException("Attendance can only be marked when session is ongoing");
        }

        var existing = attendanceRepo.findByParticipantIdAndSessionId(participantId, sessionId);

        if (existing.isPresent()) {
            SessionAttendance attendance = existing.get();
            if (attendance.getStatus() != null) {
                throw new IllegalStateException("Attendance already recorded for this participant");
            }
            // If status is null, allow marking now
            attendance.setStatus(status);
            attendance.setMarkedAt(LocalDateTime.now());
            attendance.setMarkedBy(chair);

            attendanceRepo.save(attendance);
        } else {
            SessionAttendance attendance = new SessionAttendance(
                        participantId,
                        sessionId,
                        null,
                        session,
                        status,
                        LocalDateTime.now(),
                        chair
                );

            attendanceRepo.save(attendance);
        }
    }
    @Transactional(readOnly = true)
    public List<AttendanceDto> getAttendeesWithAttendance(Long sessionId, String chairEmail) {

    Session session = sessionRepo.findById(sessionId)
            .orElseThrow(() -> new RuntimeException("Session not found"));

    User chair = userRepo.findByEmail(chairEmail)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!session.getChair().getId().equals(chair.getId())) {
        throw new AccessDeniedException("Not session chair");
    }

    // Registered participants
    List<User> participants =
        sessionRegRepo.findBySession(session)
            .stream()
            .map(SessionRegistration::getParticipant)
            .toList();

    // Attach attendance
    return participants.stream().map(user -> {

        AttendanceStatus status =
            attendanceRepo.findByParticipantAndSession(user, session)
                .map(SessionAttendance::getStatus)
                .orElse(null);

        return new AttendanceDto(
            user.getId(),
            user.getName(),
            user.getEmail(),
            status
        );
    }).toList();
}
    public Session getSessionForChair(Long sessionId, String email) {
    Session session = sessionRepo.findById(sessionId)
        .orElseThrow();

    User chair = userRepo.findByEmail(email).orElseThrow();

    if (!session.getChair().getId().equals(chair.getId())) {
        throw new AccessDeniedException("Not your session");
    }

    return session;
}


}

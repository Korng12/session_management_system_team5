package com.team5.demo.services;

import com.team5.demo.dto.CreateSessionRequest;
import com.team5.demo.dto.SessionResponse;
import com.team5.demo.dto.UpdateSessionStatusRequest;
import com.team5.demo.dto.SessionDependenciesResponse;
import com.team5.demo.dto.RoomAvailabilityResponse;
import com.team5.demo.dto.TimeConflictResponse;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.SessionStatus;
import com.team5.demo.entities.Conference;
import com.team5.demo.entities.Room;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.SessionRepository;
import com.team5.demo.repositories.ConferenceRepository;
import com.team5.demo.repositories.RoomRepository;
import com.team5.demo.repositories.SessionRegistrationRepository;
import com.team5.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionService {
    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ConferenceRepository conferenceRepository;
    @Autowired
    private SessionRegistrationRepository sessionRegistrationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create a new session
     * @param request the create session request
     * @return the created session response
     * @throws IllegalArgumentException if required fields are missing or invalid
     */
    public SessionResponse createSession(CreateSessionRequest request) {
        // Validate request
        if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Session title is required");
        }

        if (request.getTitle().trim().length() < 3) {
            throw new IllegalArgumentException("Session title must be at least 3 characters");
        }

        if (request.getTitle().trim().length() > 100) {
            throw new IllegalArgumentException("Session title must not exceed 100 characters");
        }

        if (request.getConferenceId() == null) {
            throw new IllegalArgumentException("Conference ID is required");
        }

        if (request.getStartTime() == null || request.getEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time are required");
        }

        if (request.getEndTime().isBefore(request.getStartTime()) || request.getEndTime().isEqual(request.getStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        // Fetch conference
        Conference conference = conferenceRepository.findById(request.getConferenceId())
                .orElseThrow(() -> new IllegalArgumentException("Conference not found with ID: " + request.getConferenceId()));

        // Fetch optional chair
        User chair = null;
        if (request.getChairId() != null) {
            chair = userRepository.findById(request.getChairId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getChairId()));
            
            // Check chair availability
            if (!sessionRepository.isChairAvailable(request.getChairId(), request.getStartTime(), request.getEndTime(), null)) {
                throw new IllegalArgumentException("Chair is not available during the specified time period. Please choose a different chair or time.");
            }
        }

        // Fetch optional room
        Room room = null;
        if (request.getRoomId() != null) {
            room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + request.getRoomId()));
            
            // Check room availability
            if (!sessionRepository.isRoomAvailable(request.getRoomId(), request.getStartTime(), request.getEndTime(), null)) {
                throw new IllegalArgumentException("Room is not available during the specified time period. Please choose a different room or time.");
            }
        }

        // Create and save session
        Session session = new Session();
        session.setTitle(request.getTitle().trim());
        session.setChair(chair);
        session.setRoom(room);
        session.setConference(conference);
        session.setStartTime(request.getStartTime());
        session.setEndTime(request.getEndTime());

        Session savedSession = sessionRepository.save(session);

        // Convert to response
        return convertToResponse(savedSession);
    }

    // Function for total in admin
    public long countAll() {
        return sessionRepository.count();
    }
    public SessionStatus resolveSessionStatus(Session session) {

        if (session.getStatus() == SessionStatus.CANCELLED) {
            return SessionStatus.CANCELLED;
        }

        LocalDateTime now = LocalDateTime.now();

        if (now.isBefore(session.getStartTime())) {
            return SessionStatus.SCHEDULED;
        }

        if (now.isAfter(session.getEndTime())) {
            return SessionStatus.COMPLETED;
        }

            return SessionStatus.ONGOING;
    }



    /**
     * Get all sessions for a specific conference
     */
    // public List<SessionResponse> getSessionsByConference(Long conferenceId) {
    //     return sessionRepository.findByConferenceIdAndDeletedFalse(conferenceId)
    //             .stream()
    //             .map(this::convertToResponse)
    //             .collect(Collectors.toList());
    // }
  public List<SessionResponse> getSessionsByConference(Long conferenceId) {

    List<Session> sessions =
        sessionRepository.findWithRoomByConference(conferenceId);

    return sessions.stream()
        .map(session -> {

            SessionResponse res = toResponse(session);

            long count =
                sessionRegistrationRepository
                    .countBySessionId(session.getId());

            res.setTotalRegistered((int) count); // ✅ HERE

            return res;
        })
        .toList();
}


    private SessionResponse toResponse(Session s) {

        SessionResponse r = new SessionResponse();

        r.setId(s.getId());
        r.setTitle(s.getTitle());
        r.setStartTime(s.getStartTime());
        r.setEndTime(s.getEndTime());
        r.setStatus(s.getStatus());

        if (s.getRoom() != null) {
            r.setRoomId(s.getRoom().getId());
            r.setRoomName(s.getRoom().getName());
            r.setRoomCapacity(s.getRoom().getCapacity()); // ✅ HERE
        }

        if (s.getChair() != null) {
            r.setChairName(s.getChair().getName());
        }

        return r;
    }


    /**
     * Get all sessions
     */
    public List<SessionResponse> getAllSessions() {
        return sessionRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get sessions with pagination
     */
    public Page<SessionResponse> getSessionsPage(Pageable pageable) {
        Page<Session> sessionPage = sessionRepository.findByDeletedFalse(pageable);
        List<SessionResponse> responses = sessionPage.getContent()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(responses, pageable, sessionPage.getTotalElements());
    }

    /**
     * Get session by ID
     */
    public SessionResponse getSessionById(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .filter(s -> !s.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));
        return convertToResponse(session);
    }

    /**
     * Update session
     */
    public SessionResponse updateSession(Long sessionId, CreateSessionRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));

        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            session.setTitle(request.getTitle());
        }

        if (request.getStartTime() != null) {
            session.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            session.setEndTime(request.getEndTime());
        }

        if (request.getChairId() != null) {
            User chair = userRepository.findById(request.getChairId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getChairId()));
            session.setChair(chair);
        }

        if (request.getChairId() != null) {
            User chair = userRepository.findById(request.getChairId())
                    .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + request.getChairId()));
            
            // Check chair availability when updating
            LocalDateTime startTime = request.getStartTime() != null ? request.getStartTime() : session.getStartTime();
            LocalDateTime endTime = request.getEndTime() != null ? request.getEndTime() : session.getEndTime();
            
            if (!sessionRepository.isChairAvailable(request.getChairId(), startTime, endTime, sessionId)) {
                throw new IllegalArgumentException("Chair is not available during the specified time period. Please choose a different chair or time.");
            }
            
            session.setChair(chair);
        }

        if (request.getRoomId() != null) {
            Room room = roomRepository.findById(request.getRoomId())
                    .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + request.getRoomId()));
            
            // Check room availability when updating
            LocalDateTime startTime = request.getStartTime() != null ? request.getStartTime() : session.getStartTime();
            LocalDateTime endTime = request.getEndTime() != null ? request.getEndTime() : session.getEndTime();
            
            if (!sessionRepository.isRoomAvailable(request.getRoomId(), startTime, endTime, sessionId)) {
                throw new IllegalArgumentException("Room is not available during the specified time period. Please choose a different room or time.");
            }
            
            session.setRoom(room);
        }

        if (request.getStatus() != null) {
            session.setStatus(request.getStatus());
        }

        Session updatedSession = sessionRepository.save(session);
        return convertToResponse(updatedSession);
    }

    /**
     * Delete session (soft delete)
     */
    public void deleteSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));
        
        session.setDeleted(true);
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);
    }

    /**
     * Permanently delete session (hard delete - admin only)
     */
    public void permanentlyDeleteSession(Long sessionId) {
        if (!sessionRepository.existsById(sessionId)) {
            throw new IllegalArgumentException("Session not found with ID: " + sessionId);
        }
        sessionRepository.deleteById(sessionId);
    }

    /**
     * Restore deleted session
     */
    public SessionResponse restoreSession(Long sessionId) {
        Session session = sessionRepository.findAllIncludingDeleted().stream()
                .filter(s -> s.getId().equals(sessionId) && s.isDeleted())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Deleted session not found with ID: " + sessionId));
        
        session.setDeleted(false);
        session.setUpdatedAt(LocalDateTime.now());
        Session restoredSession = sessionRepository.save(session);
        return convertToResponse(restoredSession);
    }

    /**
     * Update session status
     */
    public SessionResponse updateSessionStatus(Long sessionId, UpdateSessionStatusRequest request) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));
        
        session.setStatus(request.getStatus());
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);
        
        return convertToResponse(session);
    }

    /**
     * Convert Session entity to SessionResponse DTO
     */
    private SessionResponse convertToResponse(Session session) {
        String chairName = session.getChair() != null ? session.getChair().getName() : "N/A";
        String roomName = session.getRoom() != null ? session.getRoom().getName() : "N/A";
        String conferenceName = session.getConference() != null ? session.getConference().getTitle() : "N/A";

        SessionResponse response = new SessionResponse(
            session.getId(),
            session.getTitle(),
            chairName,
            roomName,
            conferenceName,
            session.getStartTime(),
            session.getEndTime(),
            session.getCreatedAt(),
            session.getStatus(),
            null // version field not implemented
        );

        response.setChairId(session.getChair() != null ? session.getChair().getId() : null);
        response.setRoomId(session.getRoom() != null ? session.getRoom().getId() : null);
        response.setConferenceId(session.getConference() != null ? session.getConference().getId() : null);
        response.setDeleted(session.isDeleted());

        return response;
    }

    /**
     * Check dependencies for a session before deletion
     * @param sessionId the session ID to check
     * @return SessionDependenciesResponse with dependency information
     */
    public SessionDependenciesResponse checkSessionDependencies(Long sessionId) {
        sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found with id: " + sessionId));

        // Check for session attendances
        String attendanceQuery = "SELECT COUNT(*) FROM session_attendance WHERE session_id = ?";
        Integer attendanceCount = jdbcTemplate.queryForObject(attendanceQuery, Integer.class, sessionId);

        boolean hasDependencies = attendanceCount != null && attendanceCount > 0;
        
        String message;
        if (hasDependencies) {
            message = String.format("This session has %d participant registration(s). Deleting will remove all attendances.", attendanceCount);
        } else {
            message = "No dependencies found. Session can be safely deleted.";
        }

        return new SessionDependenciesResponse(
                hasDependencies,
                attendanceCount != null ? attendanceCount : 0,
                message,
                true // canDelete is always true since we use soft delete
        );
    }

    /**
     * Check room availability for a given time period
     * @param roomId the room ID to check
     * @param startTime the start time
     * @param endTime the end time
     * @param excludeSessionId optional session ID to exclude (for updates)
     * @return RoomAvailabilityResponse with availability status and conflicting sessions
     */
    public RoomAvailabilityResponse checkRoomAvailability(Long roomId, LocalDateTime startTime, 
                                                         LocalDateTime endTime, Long excludeSessionId) {
        if (roomId == null) {
            return new RoomAvailabilityResponse(true, "No room specified");
        }

        // Verify room exists
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found with ID: " + roomId));

        // Find conflicting sessions
        List<Session> conflicts = sessionRepository.findConflictingSessions(
            roomId, 
            startTime, 
            endTime, 
            excludeSessionId != null ? excludeSessionId : -1
        );

        if (conflicts.isEmpty()) {
            return new RoomAvailabilityResponse(
                true, 
                String.format("Room '%s' is available for the specified time period", room.getName())
            );
        } else {
            List<RoomAvailabilityResponse.ConflictingSession> conflictDetails = conflicts.stream()
                .map(s -> new RoomAvailabilityResponse.ConflictingSession(
                    s.getId(),
                    s.getTitle(),
                    s.getStartTime(),
                    s.getEndTime()
                ))
                .collect(Collectors.toList());

            return new RoomAvailabilityResponse(
                false,
                String.format("Room '%s' has %d conflicting session(s) during this time", 
                    room.getName(), conflicts.size()),
                conflictDetails
            );
        }
    }

    /**
     * Check chair availability for time conflicts
     * @param chairId the chair user ID
     * @param startTime the start time
     * @param endTime the end time
     * @param excludeSessionId optional session ID to exclude (for updates)
     * @return TimeConflictResponse with availability status and conflicting sessions
     */
    public TimeConflictResponse checkChairAvailability(Long chairId, LocalDateTime startTime, 
                                                       LocalDateTime endTime, Long excludeSessionId) {
        if (chairId == null) {
            return new TimeConflictResponse(true, "No chair specified", "CHAIR");
        }

        // Verify chair exists
        User chair = userRepository.findById(chairId)
                .orElseThrow(() -> new IllegalArgumentException("Chair not found with ID: " + chairId));

        // Find conflicting sessions
        List<Session> conflicts = sessionRepository.findChairConflicts(
            chairId, 
            startTime, 
            endTime, 
            excludeSessionId != null ? excludeSessionId : -1
        );

        if (conflicts.isEmpty()) {
            return new TimeConflictResponse(
                true, 
                String.format("Chair '%s' is available for the specified time period", chair.getName()),
                "CHAIR"
            );
        } else {
            List<TimeConflictResponse.ConflictingSession> conflictDetails = conflicts.stream()
                .map(s -> new TimeConflictResponse.ConflictingSession(
                    s.getId(),
                    s.getTitle(),
                    s.getChair() != null ? s.getChair().getName() : "N/A",
                    s.getRoom() != null ? s.getRoom().getName() : "N/A",
                    s.getStartTime(),
                    s.getEndTime()
                ))
                .collect(Collectors.toList());

            return new TimeConflictResponse(
                false,
                String.format("Chair '%s' has %d conflicting session(s) during this time", 
                    chair.getName(), conflicts.size()),
                "CHAIR",
                conflictDetails
            );
        }
    }

    /**
     * Assign chair to a session with conflict checking
     * @param sessionId the session ID
     * @param chairId the chair user ID
     * @return TimeConflictResponse indicating success or conflicts
     * @throws IllegalArgumentException if session or chair not found
     */
    public TimeConflictResponse assignChairToSession(Long sessionId, Long chairId) {
        // Get the session
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));

        // Get the chair
        User chair = userRepository.findById(chairId)
                .orElseThrow(() -> new IllegalArgumentException("Chair not found with ID: " + chairId));

        // Check for conflicts
        List<Session> conflicts = sessionRepository.findChairConflicts(
            chairId, 
            session.getStartTime(), 
            session.getEndTime(), 
            sessionId  // Exclude current session from conflict check
        );

        if (!conflicts.isEmpty()) {
            // Return conflict response without assigning
            List<TimeConflictResponse.ConflictingSession> conflictDetails = conflicts.stream()
                .map(s -> new TimeConflictResponse.ConflictingSession(
                    s.getId(),
                    s.getTitle(),
                    s.getChair() != null ? s.getChair().getName() : "N/A",
                    s.getRoom() != null ? s.getRoom().getName() : "N/A",
                    s.getStartTime(),
                    s.getEndTime()
                ))
                .collect(Collectors.toList());

            return new TimeConflictResponse(
                false,
                String.format("Cannot assign chair: '%s' has %d conflicting session(s) during this time", 
                    chair.getName(), conflicts.size()),
                "CHAIR",
                conflictDetails
            );
        }

        // No conflicts - assign the chair
        session.setChair(chair);
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);

        return new TimeConflictResponse(
            true,
            String.format("Chair '%s' assigned successfully to session '%s'", chair.getName(), session.getTitle()),
            "CHAIR"
        );
    }

    /**
     * Remove chair from a session
     * @param sessionId the session ID
     * @return SessionResponse with the updated session (no chair assigned)
     * @throws IllegalArgumentException if session not found
     */
    public SessionResponse removeChairFromSession(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session not found with ID: " + sessionId));
        
        // Remove the chair assignment
        session.setChair(null);
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);

        return convertToResponse(session);
    }

    /**
     * Get sessions where a specific user is the chair (paginated)
     * @param chairId the chair user ID
     * @param pageable pagination info
     * @return Page of SessionResponse where user is chair
     */
    public Page<SessionResponse> getSessionsByChairPaginated(Long chairId, Pageable pageable) {
        Page<Session> sessions = sessionRepository.findByChairIdAndDeletedFalse(chairId, pageable);
        return sessions.map(this::convertToResponse);
    }

    /**
     * Get sessions where a specific user is the chair
     * @param chairId the chair user ID
     * @return List of SessionResponse where user is chair
     */
    public List<SessionResponse> getSessionsByChair(Long chairId) {
        List<Session> sessions = sessionRepository.findByChairIdAndDeletedFalse(chairId);
        return sessions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Check if a session is upcoming (starts in the future)
     * @param session the session response
     * @return true if session starts in the future
     */
    public boolean isUpcoming(SessionResponse session) {
        if (session.getStartTime() == null) {
            return false;
        }
        return session.getStartTime().isAfter(LocalDateTime.now());
    }

    @Transactional(readOnly = true)
    public Integer getRoomCapacity(Long sessionId) {
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        return session.getRoom().getCapacity();
    }
}

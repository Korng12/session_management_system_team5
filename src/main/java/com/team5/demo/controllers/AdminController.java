package com.team5.demo.controllers;

import com.team5.demo.dto.CreateSessionRequest;
import com.team5.demo.dto.SessionResponse;
import com.team5.demo.dto.UpdateSessionStatusRequest;
import com.team5.demo.dto.SessionDependenciesResponse;
import com.team5.demo.dto.ValidationErrorResponse;
import com.team5.demo.dto.RoomAvailabilityResponse;
import com.team5.demo.dto.TimeConflictResponse;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.StaleObjectStateException;
import com.team5.demo.dto.ConferenceDTO;
import com.team5.demo.dto.RoomDTO;
import com.team5.demo.dto.UserDTO;
import com.team5.demo.services.SessionService;

import jakarta.validation.Valid;

import com.team5.demo.repositories.ConferenceRepository;
import com.team5.demo.repositories.SessionRepository;
import com.team5.demo.repositories.RoomRepository;
import com.team5.demo.repositories.UserRepository;
import com.team5.demo.entities.Room;
import com.team5.demo.entities.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private com.team5.demo.repositories.SessionRegistrationRepository sessionRegistrationRepository;

    // Admin Dashboard
    @GetMapping("")
    public String showAdminHomepage(Model model) {
        return "admin/dashboard"; 
    }

    // Manage Users
    @GetMapping("/manage-users")
    public String manageUsers(Model model) {
        return "admin/manage_users"; 
    }
    @GetMapping("/admin/manage-schedule")
    public String manageSchedule() {
        return "admin/view-schedule";
    }
    @GetMapping("/manage-conferences")
    public String getMethodName() {
        return "admin/manage-conferences";
    }
    

    // Manage Rooms
    @GetMapping("/manage-rooms")
    public String manageRooms(Model model) {
        return "admin/manage-rooms";
    }

    // Manage Sessions
    @GetMapping("/manage-sessions")
    public String manageSessions(Model model) {
        return "admin/manage-sessions"; 
    }

    // Create Session Form
    @GetMapping("/sessions/create")
    public String showCreateSessionForm(Model model) {
        return "admin/create-session";
    }

    // Edit Session Form
    @GetMapping("/sessions/{id}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditSessionForm(@PathVariable("id") Long id, Model model) {
        try {
            sessionService.getSessionById(id);
            model.addAttribute("sessionId", id);
            return "admin/edit-session";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/manage-sessions";
        }
    }

    // Manage Schedule
    @GetMapping("/schedule")
    public String manageSchedule(Model model) {
        return "admin/schedule"; // Manage schedule view
    }

    // List Registered Users (if applicable, assuming manage-users covers this)
    // @GetMapping("/admin/view-registrations")
    @GetMapping("/view-registeredUsers")
    public String listRegisteredUsers(Model model) {
        return "admin/view-registrations"; 
    }

    // ============ SESSION CRUD OPERATIONS ============

    /**
     * Get all conferences for dropdown
     * GET /admin/api/conferences
     */
    @GetMapping("/api/conferences")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        try {
            List<ConferenceDTO> conferences = conferenceRepository.findAll()
                    .stream()
                    .map(conference -> new ConferenceDTO(
                            conference.getId(),
                            conference.getTitle(),
                            conference.getDescription()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(conferences);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get all rooms for dropdown
     * GET /admin/api/rooms
     */
    @GetMapping("/api/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoomDTO>> getAllRooms() {
        try {
            List<RoomDTO> rooms = roomRepository.findAll()
                    .stream()
                    .map(room -> new RoomDTO(
                            room.getId(),
                            room.getName(),
                            room.getCapacity() != null ? room.getCapacity().longValue() : null
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Create a new room
     * POST /admin/rooms
     */
    @PostMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRoom(@Valid @RequestBody RoomDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation failed");
            bindingResult.getFieldErrors().forEach(error -> 
                errorResponse.addError(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            Room room = new Room(request.getName(), request.getCapacity() != null ? request.getCapacity().intValue() : 50);
            Room saved = roomRepository.save(room);
            RoomDTO response = new RoomDTO(saved.getId(), saved.getName(), saved.getCapacity() != null ? saved.getCapacity().longValue() : null);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Failed to create room: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Update a room
     * PUT /admin/rooms/{id}
     */
    @PutMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateRoom(@PathVariable("id") Long id, @Valid @RequestBody RoomDTO request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation failed");
            bindingResult.getFieldErrors().forEach(error -> 
                errorResponse.addError(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found"));
            room.setName(request.getName());
            if (request.getCapacity() != null) {
                room.setCapacity(request.getCapacity().intValue());
            }
            Room saved = roomRepository.save(room);
            RoomDTO response = new RoomDTO(saved.getId(), saved.getName(), saved.getCapacity() != null ? saved.getCapacity().longValue() : null);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Failed to update room: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Delete a room
     * DELETE /admin/rooms/{id}
     */
    @DeleteMapping("/rooms/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id) {
        try {
            // Detach room from all referencing sessions to avoid FK violations
            List<Session> sessions = sessionRepository.findByRoomId(id);
            sessions.forEach(s -> s.setRoom(null));
            if (!sessions.isEmpty()) {
                sessionRepository.saveAll(sessions);
                sessionRepository.flush();
            }

            Room room = roomRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Room not found"));
            roomRepository.delete(room);
            return ResponseEntity.ok(new ValidationErrorResponse("Room deleted"));
        } catch (IllegalArgumentException e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Failed to delete room: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get all users for session chair dropdown
     * GET /admin/api/users
     */
    @GetMapping("/api/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userRepository.findAll()
                    .stream()
                    .map(user -> new UserDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get all sessions
     * GET /admin/api/sessions
     */
    @GetMapping("/api/sessions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SessionResponse>> getAllSessions() {
        try {
            List<SessionResponse> sessions = sessionService.getAllSessions();
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Get sessions with pagination
     * GET /admin/api/sessions/paginated?page=0&size=10&sort=startTime,desc
     */
    @GetMapping("/api/sessions/paginated")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<SessionResponse>> getSessionsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "startTime") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
            Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
            Page<SessionResponse> sessions = sessionService.getSessionsPage(pageable);
            return ResponseEntity.ok(sessions);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Create a new session
     * POST /admin/sessions
     * 
     * Request Body:
     * {
     *     "title": "Keynote Session",
     *     "chairId": 1,
     *     "roomId": 1,
     *     "conferenceId": 1,
     *     "startTime": "2025-01-15T09:00:00",
     *     "endTime": "2025-01-15T10:00:00"
     * }
     */
    @PostMapping("/sessions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createSession(@Valid @RequestBody CreateSessionRequest request, BindingResult bindingResult) {
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation failed");
            
            // Add field errors
            bindingResult.getFieldErrors().forEach(error -> 
                errorResponse.addError(error.getField(), error.getDefaultMessage())
            );
            
            // Add global errors (like ValidSessionTime, ValidDuration)
            bindingResult.getGlobalErrors().forEach(error ->
                errorResponse.addError("session", error.getDefaultMessage())
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        try {
            SessionResponse response = sessionService.createSession(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("An unexpected error occurred. Please try again.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Get session by ID
     * GET /admin/sessions/{id}
     */
    @GetMapping("/sessions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionResponse> getSession(@PathVariable("id") Long id) {
        try {
            SessionResponse response = sessionService.getSessionById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Get all sessions for a conference
     * GET /admin/sessions/conference/{conferenceId}
     */
    @GetMapping("/sessions/conference/{conferenceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getSessionsByConference(@PathVariable("conferenceId") Long conferenceId) {
        try {
            return ResponseEntity.ok(sessionService.getSessionsByConference(conferenceId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Update session
     * PUT /admin/sessions/{id}
     */
    @PutMapping("/sessions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSession(
            @PathVariable("id") Long id,
            @Valid @RequestBody CreateSessionRequest request,
            BindingResult bindingResult) {
        
        // Check for validation errors
        if (bindingResult.hasErrors()) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse("Validation failed");
            
            bindingResult.getFieldErrors().forEach(error -> 
                errorResponse.addError(error.getField(), error.getDefaultMessage())
            );
            
            bindingResult.getGlobalErrors().forEach(error ->
                errorResponse.addError("session", error.getDefaultMessage())
            );
            
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
        
        try {
            SessionResponse response = sessionService.updateSession(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (StaleObjectStateException e) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(
                "Concurrent edit detected: The session was modified by another user. Please refresh and try again."
            );
            errorResponse.addError("version", "CONCURRENT_EDIT");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
        }
    }

    /**
     * Check session dependencies before deletion
     * GET /admin/sessions/{id}/dependencies
     */
    @GetMapping("/sessions/{id}/dependencies")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SessionDependenciesResponse> checkSessionDependencies(@PathVariable("id") Long id) {
        try {
            SessionDependenciesResponse dependencies = sessionService.checkSessionDependencies(id);
            return ResponseEntity.ok(dependencies);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Delete session (hard delete - permanently removes from database)
     * DELETE /admin/sessions/{id}
     */
    @DeleteMapping("/sessions/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSession(@PathVariable("id") Long id) {
        try {
            sessionService.deleteSession(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Update session status
     * PATCH /admin/sessions/{id}/status
     */
    @PatchMapping("/sessions/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateSessionStatus(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateSessionStatusRequest request) {
        try {
            SessionResponse response = sessionService.updateSessionStatus(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (StaleObjectStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Concurrent edit detected: The session was modified by another user. Please refresh and try again.");
            error.put("errorCode", "CONCURRENT_EDIT");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
    }

    /**
     * Restore deleted session
     * POST /admin/sessions/{id}/restore
     */
    @PostMapping("/sessions/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> restoreSession(@PathVariable("id") Long id) {
        try {
            SessionResponse response = sessionService.restoreSession(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Session not found or not deleted");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Permanently delete session (hard delete)
     * DELETE /admin/sessions/{id}/permanent
     */
    @DeleteMapping("/sessions/{id}/permanent")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> permanentlyDeleteSession(@PathVariable("id") Long id) {
        try {
            sessionService.permanentlyDeleteSession(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Check room availability
     * GET /admin/rooms/{roomId}/availability
     */
    @GetMapping("/rooms/{roomId}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoomAvailabilityResponse> checkRoomAvailability(
            @PathVariable("roomId") Long roomId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(value = "excludeSessionId", required = false) Long excludeSessionId) {
        try {
            RoomAvailabilityResponse response = sessionService.checkRoomAvailability(
                roomId, startTime, endTime, excludeSessionId
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Check chair availability
     * GET /admin/users/{chairId}/availability
     */
    @GetMapping("/users/{chairId}/availability")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TimeConflictResponse> checkChairAvailability(
            @PathVariable("chairId") Long chairId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(value = "excludeSessionId", required = false) Long excludeSessionId) {
        try {
            TimeConflictResponse response = sessionService.checkChairAvailability(
                chairId, startTime, endTime, excludeSessionId
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Assign chair to a session
     * POST /admin/sessions/{sessionId}/assign-chair/{chairId}
     */
    @PostMapping("/sessions/{sessionId}/assign-chair/{chairId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> assignChairToSession(
            @PathVariable("sessionId") Long sessionId,
            @PathVariable("chairId") Long chairId) {
        try {
            TimeConflictResponse response = sessionService.assignChairToSession(sessionId, chairId);
            
            if (response.isHasConflict()) {
                // Chair has conflicts - return 409 Conflict with details
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
            
            // Chair assigned successfully
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new TimeConflictResponse(false, e.getMessage(), "CHAIR"));
        }
    }

    /**
     * Remove chair from a session
     * DELETE /admin/sessions/{sessionId}/remove-chair
     */
    @DeleteMapping("/sessions/{sessionId}/remove-chair")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeChairFromSession(@PathVariable("sessionId") Long sessionId) {
        try {
            SessionResponse response = sessionService.removeChairFromSession(sessionId);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new TimeConflictResponse(false, e.getMessage(), "CHAIR"));
        }
    }

    /**
     * Get attendance data with room capacity info
     * GET /admin/api/attendance
     */
    @GetMapping("/api/attendance")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> getAttendanceData() {
        try {
            List<Session> sessions = sessionRepository.findAll();
            List<Map<String, Object>> attendanceList = sessions.stream().map(session -> {
                Map<String, Object> attendance = new HashMap<>();
                attendance.put("sessionId", session.getId());
                attendance.put("sessionTitle", session.getTitle());
                attendance.put("roomName", session.getRoom() != null ? session.getRoom().getName() : "N/A");
                attendance.put("roomCapacity", session.getRoom() != null ? session.getRoom().getCapacity() : 0);
                
                // Count registered attendees for this session using SessionRegistrationRepository
                long attendeeCount = sessionRegistrationRepository.findBySession(session).size();
                
                attendance.put("attendeeCount", attendeeCount);
                attendance.put("availableSeats", session.getRoom() != null ? session.getRoom().getCapacity() - attendeeCount : 0);
                attendance.put("isFull", session.getRoom() != null && attendeeCount >= session.getRoom().getCapacity());
                
                return attendance;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Render attendance page
     * GET /admin/view-attendance
     */
    @GetMapping("/view-attendance")
    public String viewAttendance() {
        return "admin/view-attendance";
    }
}
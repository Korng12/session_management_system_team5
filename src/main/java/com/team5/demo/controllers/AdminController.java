package com.team5.demo.controllers;


import com.team5.demo.entities.Conference;
import com.team5.demo.services.ConferenceService;
import com.team5.demo.services.RegistrationService;
import com.team5.demo.entities.Registration;
import com.team5.demo.services.SessionAttendanceService;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import lombok.RequiredArgsConstructor;
import com.team5.demo.dto.CreateSessionRequest;
import com.team5.demo.dto.SessionResponse;
import com.team5.demo.dto.UpdateSessionStatusRequest;
import com.team5.demo.dto.SessionDependenciesResponse;
import com.team5.demo.dto.ValidationErrorResponse;
import com.team5.demo.dto.RoomAvailabilityResponse;
import com.team5.demo.dto.TimeConflictResponse;

import java.security.Principal;
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
import com.team5.demo.services.UserService;

import jakarta.validation.Valid;



import com.team5.demo.repositories.ConferenceRepository;
import com.team5.demo.repositories.SessionRepository;
import com.team5.demo.repositories.RoomRepository;
import com.team5.demo.repositories.UserRepository;
import com.team5.demo.entities.Room;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.User;
import com.team5.demo.entities.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.beans.factory.annotation.Autowired;





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
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private SessionAttendanceService sessionAttendanceService;
    @Autowired
    private ConferenceService conferenceService;
    @Autowired
    private com.team5.demo.repositories.SessionRegistrationRepository sessionRegistrationRepository;

    @GetMapping("")
    public String dashboard(Model model) {
        long totalRegistrations = registrationService.getTotalRegistrations();
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("totalRegistrations", totalRegistrations);
        model.addAttribute("totalSessions", sessionService.countAll());
        model.addAttribute("totalConferences", conferenceService.countAll());
        return "admin/dashboard";
    }
    

    @GetMapping("/admin/manage-schedule")
    public String manageSchedule() {
        return "admin/view-schedule";
    }
    

    // Manage Rooms
    @GetMapping("/manage-rooms")
    public String manageRooms(Model model) {
        model.addAttribute("activePage", "rooms");
        return "admin/manage-rooms";
    }

    // Manage Sessions
    @GetMapping("/manage-sessions")
    public String manageSessions(Model model) {
        model.addAttribute("activePage", "sessions");
        return "admin/manage-sessions";
    }

    /* ===================== MANAGE SCHEDULE ===================== */
    // @GetMapping("/manage-schedule")
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
        model.addAttribute("activePage", "schedule");
        return "admin/view-schedule";
    }

    /* ===================== MANAGE REGISTRATIONS (search and clear) ===================== */
    @GetMapping("/manage-registrations")
    public String manageRegistrations(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<Registration> registrations;

        if (keyword != null && !keyword.trim().isEmpty()) {
            registrations = registrationService.searchByParticipant(keyword);
        } else {
            registrations = registrationService.getAllRegistrations(); 
        }

        long totalRegistrations = registrationService.getTotalRegistrations(); 

        model.addAttribute("registrations", registrations);       
        model.addAttribute("totalRegistrations", totalRegistrations);
        model.addAttribute("keyword", keyword);
        model.addAttribute("activePage", "registrations");

        return "admin/view-registrations";
    }



    /* ===================== OLD URL REDIRECT ===================== */
    @GetMapping("/view-registeredUsers")
    public String viewRegistrations(Model model) {

        // For table
        List<Registration> registrations =
                registrationService.getAllRegistrations();

        // For summary
        long totalRegistrations =
                registrationService.getTotalRegistrations();

        model.addAttribute("registrations", registrations);       // ✅ LIST
        model.addAttribute("totalRegistrations", totalRegistrations); // ✅ LONG
        model.addAttribute("activePage", "registrations");

        return "admin/view-registrations";
    }





    /* ===================== VIEW ATTENDANCES ===================== */
    @GetMapping("/view-attendance")
        public String viewAttendances(Model model) {

            model.addAttribute(
                "attendances",
                sessionAttendanceService.getAllAttendances()
            );

            model.addAttribute("activePage", "attendances");
            return "admin/view-attendance";
        }
    /* ===================== Manage Attendance ===================== */
    @GetMapping("/manage-conferences")
    public String manageConferences(
            @RequestParam(value = "keyword", required = false) String keyword,
        Model model) {

    List<Conference> conferences;

    if (keyword != null && !keyword.trim().isEmpty()) {
        conferences = conferenceService.searchByTitle(keyword);
    } else {
        conferences = conferenceService.getAllConferences();
    }
    model.addAttribute("conference", new Conference());

    model.addAttribute("conferences", conferences);
    model.addAttribute("keyword", keyword);
    model.addAttribute("activePage", "conferences");

    return "admin/manage-conferences";
    }
 
    

    // Delete button 
    @GetMapping("/conferences/delete/{id}")
    public String deleteConference(@PathVariable("id") Long id,
                                RedirectAttributes ra) {
        try {
            conferenceService.delete(id);
            ra.addFlashAttribute("success", "Conference deleted successfully");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/manage-conferences";
    }


    // Insert and Delete 
    // @PostMapping("/conferences/save")
    // public String saveConference(Conference conference){
    //     conferenceService.save(conference);
    //     return "redirect:/admin/manage-conferences";
    // }
 @PostMapping("/conferences/save")
public String saveConference(
        @Valid @ModelAttribute("conference") ConferenceDTO dto,
        BindingResult result,
        Model model
) {

    // SIMPLE cross-field validation
    if (!result.hasErrors()
        && dto.getEndDate().isBefore(dto.getStartDate())) {
        result.reject(
            "date.order",
            "End date must be after start date"
        );
    }

    if (result.hasErrors()) {
        model.addAttribute("conferences",
                conferenceService.getAllConferences());
                model.addAttribute("showModal", true);

        return "admin/manage-conferences";
    }

    conferenceService.saveFromDto(dto);
    return "redirect:/admin/conferences";
}

    


    /**
     * Get all conferences for dropdown
     * GET /admin/api/conferences
     */
    @GetMapping("/api/conferences")
    @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<List<ConferenceDTO>> getAllConferences() {
        try {
            java.time.LocalDate today = java.time.LocalDate.now();
            List<ConferenceDTO> conferences = conferenceRepository.findAll()
                .stream()
                // Only conferences that are not outdated (no endDate or endDate >= today)
                .filter(conf -> conf.getEndDate() == null || !conf.getEndDate().isBefore(today))
                .map(conference -> new ConferenceDTO(
                    conference.getId(),
                    conference.getTitle(),
                    conference.getDescription(),
                    conference.getStartDate(),
                    conference.getEndDate()
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
     * Get all users with CHAIR role for session chair assignment
     * GET /admin/api/users/chairs
     */
    @GetMapping("/api/users/chairs")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getChairUsers() {
        try {
            List<UserDTO> chairs = userRepository.findAll()
                    .stream()
                    .filter(user -> user.getRoles().stream()
                            .anyMatch(role -> role.getName().equals("CHAIR")))
                    .map(user -> new UserDTO(
                            user.getId(),
                            user.getName(),
                            user.getEmail()
                    ))
                    .collect(Collectors.toList());
            System.out.println("Found " + chairs.size() + " chair users: " + chairs);
            return ResponseEntity.ok(chairs);
        } catch (Exception e) {
            System.err.println("Error fetching chair users: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Search user by email
     * GET /admin/api/users/search-by-email?email=
     */
    @GetMapping("/api/users/search-by-email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email)
                .map(u -> ResponseEntity.ok(new UserDTO(u.getId(), u.getName(), u.getEmail())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Search user by id
     * GET /admin/api/users/search-by-id?id=
     */
    @GetMapping("/api/users/search-by-id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> getUserById(@RequestParam Long id) {
        return userRepository.findById(id)
                .map(u -> ResponseEntity.ok(new UserDTO(u.getId(), u.getName(), u.getEmail())))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
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
                
                // Get attendance count
                long attendanceCount = sessionAttendanceService.getAttendanceCountBySession(session.getId());
                attendance.put("attendanceCount", attendanceCount);
                
                return attendance;
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // User Crude
    @GetMapping("/manage-users")
public String manageUsers() {
    return "redirect:/admin/users";
}

@GetMapping("/users")
public String listUsers(Model model) {
    model.addAttribute("users", userService.findAll());
    return "admin/manage-users";
}

@PostMapping("/users/create")
public String createUser(@RequestParam("name") String name,
                         @RequestParam("email") String email,
                         @RequestParam("password") String password,
                         @RequestParam("role") String role) {
    userService.create(name, email, password, role);
    return "redirect:/admin/users";
}

@PostMapping("/users/{id}/update")
public String updateUser(@PathVariable("id") Long id,
                         @RequestParam("name") String name,
                         @RequestParam("email") String email,
                         @RequestParam(value="password", required=false) String password,
                         @RequestParam("role") String role) {
    userService.update(id, name, email, password, role);
    return "redirect:/admin/users";
}

@DeleteMapping("/users/{id}")
public String deleteUser(@PathVariable("id") Long id,Principal principal, RedirectAttributes ra) {
    try {
        userService.delete(id,principal.getName());
        ra.addFlashAttribute("success", "User deleted");
    } catch (Exception e) {
        ra.addFlashAttribute("error", e.getMessage());
    }
    return "redirect:/admin/users";
}
}

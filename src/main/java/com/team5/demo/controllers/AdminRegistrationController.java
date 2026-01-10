package com.team5.demo.controllers;

import com.team5.demo.entities.*;
import com.team5.demo.repositories.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRegistrationController {

    private final UserRepository userRepository;
    private final ConferenceRepository conferenceRepository;
    private final SessionRepository sessionRepository;
    private final RegistrationRepository registrationRepository;
    private final SessionAttendanceRepository sessionAttendanceRepository;
    private final AdminRegistrationViewRepository adminViewRepository;

    public AdminRegistrationController(
            UserRepository userRepository,
            ConferenceRepository conferenceRepository,
            SessionRepository sessionRepository,
            RegistrationRepository registrationRepository,
            SessionAttendanceRepository sessionAttendanceRepository,
            AdminRegistrationViewRepository adminViewRepository
    ) {
        this.userRepository = userRepository;
        this.conferenceRepository = conferenceRepository;
        this.sessionRepository = sessionRepository;
        this.registrationRepository = registrationRepository;
        this.sessionAttendanceRepository = sessionAttendanceRepository;
        this.adminViewRepository = adminViewRepository;
    }

    // PAGE
    @GetMapping("/manage-registrations")
    public String manageRegistrations(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("conferences", conferenceRepository.findAll());
        model.addAttribute("registrations",
                adminViewRepository.findAdminRegistrationView());
        return "admin/manage-registrations";
    }

    // AJAX – sessions by conference (NO recursion)
    @GetMapping("/conferences/{id}/sessions")
    @ResponseBody
    public List<Session> getSessions(@PathVariable("id") Long id) {
        return sessionRepository.findByConferenceIdAndDeletedFalse(id);
    }

    // CREATE
    @PostMapping("/registrations")
    public String register(
            @RequestParam("userId") Long userId,
            @RequestParam("conferenceId") Long conferenceId,
            @RequestParam(value = "sessionId", required = false) Long sessionId
    ) {

        if (!registrationRepository
                .existsByParticipantIdAndConferenceId(userId, conferenceId)) {

            Registration r = new Registration();
            r.setParticipant(userRepository.findById(userId).orElseThrow());
            r.setConference(conferenceRepository.findById(conferenceId).orElseThrow());
            r.setStatus("CONFIRMED");
            r.setRegisteredAt(LocalDateTime.now());
            registrationRepository.save(r);
        }

        if (sessionId != null &&
            !sessionAttendanceRepository
                    .existsByParticipantIdAndSessionId(userId, sessionId)) {

            SessionAttendance sa = new SessionAttendance();
            sa.setParticipant(userRepository.findById(userId).orElseThrow());
            sa.setSession(sessionRepository.findById(sessionId).orElseThrow());
            sa.setStatus("REGISTERED"); // IMPORTANT
            sa.setAttendedAt(LocalDateTime.now());
            sessionAttendanceRepository.save(sa);
        }

        return "redirect:/admin/manage-registrations";
    }

    @PostMapping("/registrations/{id}/status")
    public String updateStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status
    ) {
        Registration r = registrationRepository.findById(id).orElseThrow();
        r.setStatus(status);
        registrationRepository.save(r);
        return "redirect:/admin/manage-registrations";
    }

    @PostMapping("/registrations/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        registrationRepository.deleteById(id);
        return "redirect:/admin/manage-registrations";
    }
}

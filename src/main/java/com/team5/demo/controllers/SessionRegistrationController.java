package com.team5.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team5.demo.services.SessionRegistrationService;

@Controller
@RequestMapping("/sessions")
public class SessionRegistrationController {

    private final SessionRegistrationService sessionRegService;

    public SessionRegistrationController(SessionRegistrationService sessionRegService) {
        this.sessionRegService = sessionRegService;
    }

    @PostMapping("/{id}/register")
    @PreAuthorize("hasAuthority('ROLE_ATTENDEE')")
    public String registerSession(
            @PathVariable("id") Long sessionId,
            Authentication auth,
            RedirectAttributes redirectAttributes) {

        try {
            sessionRegService.registerForSession(sessionId, auth.getName());
            redirectAttributes.addFlashAttribute(
                "successMessage", "Session registered successfully"
            );
        } catch (IllegalStateException ex) {
            redirectAttributes.addFlashAttribute(
                "errorMessage", ex.getMessage()
            );
        }

        // Redirect back (important!)
        return  "redirect:/schedule-attendance";

    }
}

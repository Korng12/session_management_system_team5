package com.team5.demo.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.team5.demo.services.ChairService;



@Controller
public class ChairController {

    @Autowired
    ChairService chairService;


    @GetMapping("/chair-dashboard")
    public String chairDashboard() {
        return "chair/chair-dashboard";
    }

    @GetMapping("/chair-profile")
    public String chairProfile() {
        return "chair/chair-profile";
    }
    @GetMapping("/chair/sessions")
    @PreAuthorize("hasAuthority('ROLE_CHAIR')")
    public String chairSessions(Authentication auth, Model model) {

        model.addAttribute(
            "sessions",
            chairService.getChairedSessions(auth.getName())
        );

        return "chair/chair-sessions";
    }
    @GetMapping("chair/sessions/{id}/attendance")
    public String manageAttendance(
            @PathVariable("id") Long sessionId,
            Authentication auth,
            Model model) {
                System.out.println("session name "+chairService.getSessionForChair(sessionId, auth.getName()).getTitle());
        model.addAttribute("currentSess",chairService.getSessionForChair(sessionId, auth.getName()));        
        
        model.addAttribute(
            "attendees",
            chairService.getAttendeesWithAttendance(sessionId, auth.getName())
        );

        return "chair/manage-attendance";
    }

    @PostMapping("chair/sessions/{sid}/attendance")
    @PreAuthorize("hasAuthority('ROLE_CHAIR')")
    public String submitAttendance(
            @PathVariable("sid") Long sessionId,
            @RequestParam("participantId") Long participantId,
            @RequestParam("status") String status,
            Authentication auth,
            RedirectAttributes redirectAttrs) {

        try {
            chairService.markAttendance(sessionId, participantId, com.team5.demo.entities.AttendanceStatus.valueOf(status), auth.getName());
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/chair/sessions/" + sessionId + "/attendance";
    }

}

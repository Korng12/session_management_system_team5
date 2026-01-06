package com.team5.demo.controllers;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ChairController {

    @GetMapping("/chair/dashboard")
public String chairDashboard(Model model) {

    model.addAttribute("assignedSessionsCount", 0);
    model.addAttribute("assignedRoomsCount", 0);
    model.addAttribute("pendingAttendanceCount", 0);
    model.addAttribute("sessions", List.of());
    return "chair/chair-dashboard";
}      

    @GetMapping("/chair-sessions")
    public String chairSessions() {
        return "chair/chair-sessions";
    }

    @GetMapping("/manage-attendance")
    public String manageAttendance() {
        return "chair/manage-attendance";
    }

    @GetMapping("/chair-profile")
    public String chairProfile() {
        return "chair/chair-profile";
    }
}

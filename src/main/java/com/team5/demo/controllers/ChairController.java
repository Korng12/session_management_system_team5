package com.team5.demo.controllers;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/chair")
public class ChairController {
    @GetMapping("/chair_dashboard")
    public String chairDashboard() {
        return "chair/chair-dashboard";
    }

    @GetMapping("/chair_sessions")
    public String chairSessions() {
        return "chair/chair-sessions";
    }

    @GetMapping("/manage_attendance")
    public String manageAttendance() {
        return "chair/manage-attendance";
    }

    @GetMapping("/chair_profile")
    public String chairProfile() {
        return "chair/chair-profile";
    }
}

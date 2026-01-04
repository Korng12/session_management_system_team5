package com.team5.demo.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
 @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/manage_rooms")
    public String manageRooms() {
        return "admin/manage-rooms";
    }

    @GetMapping("/manage_sessions")
    public String manageSessions() {
        return "admin/manage-sessions";
    }

    @GetMapping("/view_registrations")
    public String viewRegistrations() {
        return "admin/view-registrations";
    }

    @GetMapping("/manage_schedule")
    public String manageSchedule() {
        return "admin/manage-schedule";
    }

    @GetMapping("/view_attendance")
    public String viewAttendance() {
        return "admin/view-attendance";
    }
}
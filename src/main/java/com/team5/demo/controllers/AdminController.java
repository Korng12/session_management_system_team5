package com.team5.demo.controllers;

import com.team5.demo.entities.Session;
import com.team5.demo.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final SessionService sessionService;

    /* ===================== DASHBOARD ===================== */
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
    }

    /* ===================== MANAGE USERS ===================== */
    @GetMapping("/manage-users")
    public String manageUsers(Model model) {
        model.addAttribute("activePage", "users");
        return "admin/manage_users";
    }
    @GetMapping("/admin/manage-schedule")
    public String manageSchedule() {
        return "admin/view-schedule";
    }
    

    // Manage Rooms
    @GetMapping("/admin/manage-rooms")
    public String manageRooms(Model model) {
        model.addAttribute("activePage", "rooms");
        return "admin/manage-rooms";
    }

    /* ===================== MANAGE SESSIONS ===================== */
    @GetMapping("/manage-sessions")
    public String manageSessions(Model model) {
        model.addAttribute("activePage", "sessions");
        return "admin/manage-sessions";
    }

    /* ===================== MANAGE SCHEDULE ===================== */
    @GetMapping("/manage-schedule")
    public String manageSchedule(Model model) {
        return "admin/schedule"; // Manage schedule view
    }

    // List Registered Users (if applicable, assuming manage-users covers this)
    @GetMapping("/admin/view-registeredUsers")
    public String listRegisteredUsers(Model model) {
        return "admin/view-registrations"; 
    }
}

package com.team5.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    // Admin Dashboard
    @GetMapping("/admin")
    public String showAdminHomepage(Model model) {
        return "admin/dashboard"; 
    }

    // Manage Users
    @GetMapping("/admin/manage-users")
    public String manageUsers(Model model) {
        return "admin/manage_users"; 
    }

    // Manage Rooms
    @GetMapping("/admin/manage-rooms")
    public String manageRooms(Model model) {
        return "admin/manage-rooms";
    }
    @GetMapping("/admin/manage-sessions")
    public String manageSessions(Model model) {
        return "admin/manage-sessions"; 
    }

    // Manage Schedule
    @GetMapping("/admin/schedule")
    public String manageSchedule(Model model) {
        return "admin/schedule"; // Manage schedule view
    }

    // List Registered Users (if applicable, assuming manage-users covers this)
    @GetMapping("/admin/view-registeredUsers")
    public String listRegisteredUsers(Model model) {
        return "admin/view-registrations"; 
    }
}
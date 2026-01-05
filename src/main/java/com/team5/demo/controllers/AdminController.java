package com.team5.demo.controllers;

import com.team5.demo.entities.Session;
import com.team5.demo.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final SessionService sessionService;

    /* ===================== DASHBOARD ===================== */
    @GetMapping("/admin")
    public String showAdminHomepage() {
        return "admin/dashboard";
    }

    /* ===================== MANAGE USERS ===================== */
    @GetMapping("/admin/manage-users")
    public String manageUsers() {
        return "admin/manage_users";
    }

    /* ===================== MANAGE ROOMS ===================== */
    @GetMapping("/admin/manage-rooms")
    public String manageRooms() {
        return "admin/manage-rooms";
    }

    /* ===================== MANAGE SESSIONS ===================== */
    @GetMapping("/admin/manage-sessions")
    public String manageSessions() {
        return "admin/manage-sessions";
    }

    /* ===================== MANAGE SCHEDULE ===================== */
    @GetMapping("/admin/schedule")
    public String manageSchedule(Model model) {

        List<Session> sessions = sessionService.getAllSessions();
        model.addAttribute("sessions", sessions);

        return "admin/schedule";
    }

    /* ===================== DELETE SESSION ===================== */
    @GetMapping("/admin/schedule/delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {

        sessionService.deleteSession(id);
        return "redirect:/admin/schedule";
    }

    /* ===================== UPDATE SESSION (POPUP) ===================== */
    @PostMapping("/admin/schedule/update")
    public String updateSchedule(
            @RequestParam("id") Long id,
            @RequestParam("title") String title
    ) {

        sessionService.updateTitle(id, title);
        return "redirect:/admin/schedule";
    }

    /* ===================== VIEW REGISTRATIONS ===================== */
    @GetMapping("/admin/view-registeredUsers")
    public String listRegisteredUsers() {
        return "admin/view-registrations";
    }
}

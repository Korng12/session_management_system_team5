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

    /* ===================== MANAGE ROOMS ===================== */
    @GetMapping("/manage-rooms")
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

        List<Session> sessions = sessionService.getAllSessions();

        // DEBUG (you can remove later)
        System.out.println(">>> ADMIN SCHEDULE - SESSION COUNT = " + sessions.size());

        model.addAttribute("activePage", "schedule");
        model.addAttribute("sessions", sessions);

        return "admin/view-schedule";
    }

    /* ===================== ALIAS ===================== */
    // Allows /admin/schedule to also work
    @GetMapping("/schedule")
    public String scheduleAlias(Model model) {
        return manageSchedule(model);
    }

    /* ===================== DELETE SESSION ===================== */
    @PostMapping("/manage-schedule/delete/{id}")
    public String deleteSession(@PathVariable Long id) {
        sessionService.deleteSession(id);
        return "redirect:/admin/manage-schedule";
    }

    /* ===================== UPDATE SESSION ===================== */
    @PostMapping("/manage-schedule/update")
    public String updateSession(
            @RequestParam Long id,
            @RequestParam String title
    ) {
        sessionService.updateTitle(id, title);
        return "redirect:/admin/manage-schedule";
    }

    /* ===================== VIEW REGISTRATIONS ===================== */
    @GetMapping("/view-registeredUsers")
    public String viewRegistrations(Model model) {
        model.addAttribute("activePage", "registrations");
        return "admin/view-registrations";
    }
}

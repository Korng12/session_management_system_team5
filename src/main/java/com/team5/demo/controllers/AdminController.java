package com.team5.demo.controllers;

import com.team5.demo.services.RegistrationService;
import com.team5.demo.services.SessionAttendanceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final RegistrationService registrationService;
    private final SessionAttendanceService sessionAttendanceService;

    /* ===================== DASHBOARD ===================== */
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("activePage", "dashboard");
        return "admin/dashboard";
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
        model.addAttribute("activePage", "schedule");
        return "admin/view-schedule";
    }

    /* ===================== MANAGE REGISTRATIONS (SEARCH + RESET) ===================== */
    @GetMapping("/manage-registrations")
public String manageRegistrations(
        @RequestParam(value = "keyword", required = false) String keyword,
        Model model) {

    if (keyword != null && !keyword.trim().isEmpty()) {
        model.addAttribute(
                "registrations",
                registrationService.searchByParticipant(keyword)
        );
    } else {
        model.addAttribute(
                "registrations",
                registrationService.getAllRegistrations()
        );
    }

    model.addAttribute("keyword", keyword);
    model.addAttribute("activePage", "registrations");

    return "admin/view-registrations"; 
}


    /* ===================== OLD URL REDIRECT ===================== */
    @GetMapping("/view-registeredUsers")
    public String viewRegistrations() {
        return "redirect:/admin/manage-registrations";
    }

    /* ===================== VIEW ATTENDANCES ===================== */
    @GetMapping("/view-attendance")
        public String viewAttendances(Model model) {

            model.addAttribute(
                "attendances",
                sessionAttendanceService.getAllAttendances()
            );

            model.addAttribute("activePage", "attendances");
            return "admin/view-attendance";
        }

}

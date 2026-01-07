package com.team5.demo.controllers;

import com.team5.demo.entities.Conference;
import com.team5.demo.services.ConferenceService;
import com.team5.demo.services.RegistrationService;
import com.team5.demo.services.SessionAttendanceService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final RegistrationService registrationService;
    private final SessionAttendanceService sessionAttendanceService;
    private final ConferenceService conferenceService;

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

    /* ===================== MANAGE REGISTRATIONS (search and clear) ===================== */
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
    /* ===================== Manage Attendance ===================== */
    @GetMapping("/manage-conferences")
    public String manageConferences(Model model) {
        model.addAttribute("conferences", conferenceService.getAllConferences());
        model.addAttribute("activePage", "conferences");
        return "admin/manage-conferences";
    }

    // Delete button 
    @GetMapping("/conferences/delete/{id}")
    public String deleteConference(@PathVariable Long id) {
        conferenceService.delete(id);
        return "redirect:/admin/manage-conferences";
    }


    // Insert and Delete 
    @PostMapping("/conferences/save")
    public String saveConference(Conference conference){
        conferenceService.save(conference);
        return "redirect:/admin/manage-conferences";
    }
    
    

    

}

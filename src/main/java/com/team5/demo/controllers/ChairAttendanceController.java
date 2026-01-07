package com.team5.demo.controllers;

import com.team5.demo.services.SessionAttendanceService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/chair/attendance")
public class ChairAttendanceController {

    private final SessionAttendanceService service;

    public ChairAttendanceController(SessionAttendanceService service) {
        this.service = service;
    }

    @GetMapping
    public String manageAttendance(Model model) {
        var attendances = service.findAll();

        model.addAttribute("attendances", attendances);
        model.addAttribute("totalCount", attendances.size());

        return "chair/manage-attendance";
    }

    @GetMapping("/remove")
    public String removeAttendance(
            @RequestParam("p") Long participantId,
            @RequestParam("s") Long sessionId
    ) {
        service.remove(participantId, sessionId);
        return "redirect:/chair/attendance";
    }
}

package com.team5.demo.controllers;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team5.demo.entities.Conference;
import com.team5.demo.services.ConferenceService;
import com.team5.demo.services.SessionRegistrationService;
import com.team5.demo.services.SessionService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;
    private final SessionService sessionService;
    private final com.team5.demo.services.RegistrationService registrationService;
 
    private final SessionRegistrationService sessionRegistrationService;
    public ConferenceController(ConferenceService conferenceService, SessionService sessionService, com.team5.demo.services.RegistrationService registrationService,SessionRegistrationService sessionRegistrationService) {
        this.conferenceService = conferenceService;
        this.sessionService=sessionService;
        this.registrationService = registrationService;
        this.sessionRegistrationService=sessionRegistrationService;
    }

    // GET /conferences
    @GetMapping
    public String listConferences(Model model) {
        var conferences = conferenceService.getAllConferences();
        model.addAttribute("conferences", conferences);
        return "user/conferences";
    }


    // GET /conferences/{id}
    @GetMapping("/{id}")
<<<<<<< HEAD

    public String conferenceDetail(
        @PathVariable Long id,
        Authentication auth,
        Model model) {

    // 1. Conference data
    model.addAttribute("conference", conferenceService.getConference(id));

    // 2. Sessions
    model.addAttribute(
        "sessions",
        sessionService.getSessionsByConference(id)
    );

    // 3. Conference registration status
    @GetMapping("/{id}")
    public String conferenceDetails(@PathVariable("id") Long id, Model model, Authentication authentication) {
        model.addAttribute("conference", conferenceService.getConference(id));
        model.addAttribute("sessions", sessionService.getSessionsByConference(id));
        boolean isRegistered = false;
        Set<Long> registeredSessionIds = Set.of();
        Long registrationId = null;
        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            isRegistered = registrationService.isRegisteredForConference(email, id);
            if (isRegistered) {
                registeredSessionIds = sessionRegistrationService.getRegisteredSessionIds(email, id);
                var opt = registrationService.getRegistrationForUserAndConference(email, id);
                if (opt.isPresent()) {
                    registrationId = opt.get().getId();
                }
            }
        }
        model.addAttribute("isRegistered", isRegistered);
        model.addAttribute("registeredSessionIds", registeredSessionIds);
        model.addAttribute("registrationId", registrationId);
        return "user/conference-detail";
    }
        boolean isRegistered = false;

        Long registrationId = null;

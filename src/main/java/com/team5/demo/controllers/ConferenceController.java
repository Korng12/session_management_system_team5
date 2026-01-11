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
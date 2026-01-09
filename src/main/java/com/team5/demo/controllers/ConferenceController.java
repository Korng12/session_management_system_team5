package com.team5.demo.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team5.demo.services.ConferenceService;
import com.team5.demo.services.SessionService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;


@Controller
@RequestMapping("/conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;
    private final SessionService sessionService;
    private final com.team5.demo.services.RegistrationService registrationService;
    public ConferenceController(ConferenceService conferenceService, SessionService sessionService, com.team5.demo.services.RegistrationService registrationService) {
        this.conferenceService = conferenceService;
        this.sessionService=sessionService;
        this.registrationService = registrationService;
    }

    // GET /conferences
@GetMapping
public String listConferences(Model model) {
    System.out.println(">>> /conferences controller called");

    var conferences = conferenceService.getAvailableConferences();
    
    System.out.println(">>> conferences size = " + conferences.size());
    model.addAttribute("conferences", conferences);

    return "user/conferences";
}


    // GET /conferences/{id}
    @GetMapping("/{id}")
    public String conferenceDetails(@PathVariable("id") Long id, Model model, Authentication authentication,Principal principal) {
        model.addAttribute("conference",
                conferenceService.getConference(id));
        
        model.addAttribute("sessions", sessionService.getSessionsByConference(id));  
        System.out.println(sessionService.getSessionsByConference(id).size());

        boolean isRegistered = false;
        Long registrationId = null;

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            try {
                var opt = registrationService.getRegistrationForUserAndConference(email, id);
                if (opt.isPresent()) {
                    isRegistered = true;
                    registrationId = opt.get().getId();
                }
            } catch (RuntimeException e) {
                // ignore, leave isRegistered false
            }
        }
        System.out.println("This is the principle"+principal.getName());

        model.addAttribute("isRegistered", isRegistered);
        model.addAttribute("registrationId", registrationId);

        return "user/conference-detail";
    }
 
}

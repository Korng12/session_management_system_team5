package com.team5.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team5.demo.services.ConferenceService;
import com.team5.demo.services.SessionService;

@Controller
@RequestMapping("/conferences")
public class ConferenceController {

    private final ConferenceService conferenceService;
    private final SessionService sessionService;
    public ConferenceController(ConferenceService conferenceService, SessionService sessionService) {
        this.conferenceService = conferenceService;
        this.sessionService=sessionService;
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
    public String conferenceDetails(@PathVariable("id") Long id, Model model) {
        model.addAttribute("conference",
                conferenceService.getConference(id));
        
        model.addAttribute("sessions", sessionService.getSessionsByConference(id));  
        System.out.println(sessionService.getSessionsByConference(id).size());   
        return "user/conference-detail";
    }
    
}

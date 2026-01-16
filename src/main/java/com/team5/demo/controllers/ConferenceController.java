package com.team5.demo.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.team5.demo.dto.SessionResponse;
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
        System.out.println(">>> /conferences controller called");

        // Show all conferences on the listing so completed ones are visible (but cannot be registered)
        var conferences = conferenceService.getAllConferences();
        
        System.out.println(">>> conferences size = " + conferences.size());
        model.addAttribute("conferences", conferences);

        return "user/conferences";
    }
    @GetMapping("/title")
    public String getMethodName(@RequestParam("title") String title,Model model) {
        var conferences=conferenceService.getConferencesByTittle(title);
        System.out.println("Size in user search conf"+conferences.size());
        model.addAttribute("conferences",conferences);
        return "user/conferences";
    }
    


    // GET /conferences/{id}
    @GetMapping("/{id}")
    public String conferenceDetail(
        @PathVariable("id") Long id,
        Authentication auth,
        Model model) {
            
        // 1. Conference data
        model.addAttribute("conference", conferenceService.getConference(id));
        List<SessionResponse> sessions = sessionService.getSessionsByConference(id);
        List<String> rooms = sessions.stream()
        .map(SessionResponse::getRoomName)
        .filter(Objects::nonNull)
        .distinct()
        .toList();
        Map<String, List<SessionResponse>> sessionsByRoom =
        sessions.stream()
            .filter(s -> s.getRoomName() != null)
            .collect(Collectors.groupingBy(SessionResponse::getRoomName));
        
        model.addAttribute("sessionsByRoom", sessionsByRoom);


        model.addAttribute("rooms", rooms);

        // 2. Sessions
        model.addAttribute(
            "sessions",
            sessions
        );

        // 3. Conference registration status
        boolean isRegistered = false;
        Set<Long> registeredSessionIds = Set.of();

        if (auth != null && auth.isAuthenticated()) {
            String email = auth.getName();

            isRegistered = registrationService
                .isRegisteredForConference(email, id);
            // var conference = conferenceService.getConference(id);
            
            if (isRegistered) {
                model.addAttribute("registrationId",registrationService.getRegistrationForUserAndConference(email, id).get().getId());
                registeredSessionIds =
                    sessionRegistrationService
                        .getRegisteredSessionIds(email, id);
            }
        }

        model.addAttribute("isRegistered", isRegistered);
        model.addAttribute("registeredSessionIds", registeredSessionIds);

    return "user/conference-detail";
   }

//    @PostMapping("/admin/conferences/save")
//     public String saveConference(
//             @ModelAttribute("conference") Conference conference,
//             Model model) {

//         try {
//             conferenceService.save(conference);
//             return "redirect:/admin/conferences";

//         } catch (IllegalArgumentException e) {

//             // REQUIRED for Thymeleaf
//             model.addAttribute("conference", conference);
//             model.addAttribute("errorMessage", e.getMessage());

//             // REQUIRED list again
//             model.addAttribute("conferences",
//                     conferenceService.findAll());

//             return "admin/manage-conferences";
//         }
//     }



}

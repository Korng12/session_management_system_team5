package com.team5.demo.controllers;

import java.security.Principal;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
//     public String conferenceDetails(@PathVariable("id") Long id, Model model, Authentication authentication,Principal principal) {
//         model.addAttribute("conference",
//                 conferenceService.getConference(id));
        
//         model.addAttribute("sessions", sessionService.getSessionsByConference(id));  
//         System.out.println(sessionService.getSessionsByConference(id).size());

//         boolean isRegistered = false;
//         Long registrationId = null;

//         if (authentication != null && authentication.isAuthenticated()) {
//             String email = authentication.getName();
//             try {
//                 var opt = registrationService.getRegistrationForUserAndConference(email, id);
//                 if (opt.isPresent()) {
//                     isRegistered = true;
//                     registrationId = opt.get().getId();
//                 }
//             } catch (RuntimeException e) {
//                 // ignore, leave isRegistered false
//             }
//         }
//         System.out.println("This is the principle"+principal.getName());
// // 3. Conference registration status
  
//     Set<Long> registeredSessionIds = Set.of();

//     if (auth != null && auth.isAuthenticated()) {
//         String email = auth.getName();

//         isRegistered = registrationService
//                 .isRegisteredForConference(email, id);

//         if (isRegistered) {
//             registeredSessionIds =
//                 sessionRegistrationService
//                     .getRegisteredSessionIds(email, id);
//         }
//     }

//     model.addAttribute("isRegistered", isRegistered);
//     model.addAttribute("registeredSessionIds", registeredSessionIds);
//         model.addAttribute("isRegistered", isRegistered);
//         model.addAttribute("registrationId", registrationId);

//         return "user/conference-detail";
//     }
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
    boolean isRegistered = false;
    Set<Long> registeredSessionIds = Set.of();
    Long registrationId = null;

    if (auth != null && auth.isAuthenticated()) {
        String email = auth.getName();

        isRegistered = registrationService
            .isRegisteredForConference(email, id);

        if (isRegistered) {
            registeredSessionIds =
                sessionRegistrationService
                    .getRegisteredSessionIds(email, id);

            // Retrieve the registration entity so we can expose its ID to the template
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
 
}

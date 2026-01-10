
package com.team5.demo.controllers;

import com.team5.demo.entities.Registration;
import com.team5.demo.repositories.RegistrationRepository;
import com.team5.demo.repositories.UserRepository;
import com.team5.demo.repositories.ConferenceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConferenceRepository conferenceRepository;

    @Autowired
    private RegistrationRepository registrationRepository;
    @GetMapping("/manage-registrations")
    public String manageRegistrations(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("conferences", conferenceRepository.findAll());
        model.addAttribute("registrations", registrationRepository.findAll());
        return "admin/manage-registrations";
    }

    @PostMapping("/registrations")
    public String registerUserToConference(
            @RequestParam("userId") Long userId,
            @RequestParam("conferenceId") Long conferenceId) {

        boolean exists =
                registrationRepository.existsByParticipantIdAndConferenceId(userId, conferenceId);

        if (!exists) {
            Registration r = new Registration();
            r.setParticipant(userRepository.findById(userId).orElseThrow());
            r.setConference(conferenceRepository.findById(conferenceId).orElseThrow());
            r.setStatus("CONFIRMED");
            r.setRegisteredAt(LocalDateTime.now());
            registrationRepository.save(r);
        }

        return "redirect:/admin/manage-registrations";
    }
    @PostMapping("/registrations/{id}/status")
    public String updateStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {

        Registration r = registrationRepository.findById(id).orElseThrow();
        r.setStatus(status);
        registrationRepository.save(r);

        return "redirect:/admin/manage-registrations";
    }

    @PostMapping("/registrations/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        registrationRepository.deleteById(id);
        return "redirect:/admin/manage-registrations";
    }
}
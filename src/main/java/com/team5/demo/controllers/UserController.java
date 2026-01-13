package com.team5.demo.controllers;

import com.team5.demo.dto.UserScheduleDto;
import com.team5.demo.entities.Registration;
import com.team5.demo.entities.Role;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.RoleRepository;
import com.team5.demo.repositories.UserRepository;
import com.team5.demo.services.ChairService;
import com.team5.demo.services.RegistrationService;
import com.team5.demo.services.SessionAttendanceService;
import com.team5.demo.services.ScheduleService;
import com.team5.demo.services.SessionAttendanceService;

import jakarta.servlet.http.HttpServletRequest;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller


public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RegistrationService registrationService;
    @Autowired 
    private ScheduleService scheduleService;

    @Autowired
    private ChairService chairService;

    // @Autowired
    // private SessionAttendanceService sessionAttendanceService;


    /* ===================== PUBLIC PAGES ===================== */

    @Autowired
    private SessionAttendanceService sessionAttendanceService;


    /* ===================== PUBLIC PAGES ===================== */

    @GetMapping("/")
    public String getLandingPage() {
        return "public/index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "public/login";
    }

    // @GetMapping("/register")
    // public String getRegisterPage() {
    //     return "public/register";
    // }

    @GetMapping("/register-conference")
    public String getRegisterConferencePage() {
        return "public/registration";
    }

    

    @GetMapping("/profile")
    public String getProfilePage(Principal principal, Model model) {
        if (principal != null) {
            String email = principal.getName();
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null) {
                model.addAttribute("userName", user.getName());
                model.addAttribute("userEmail", user.getEmail());
                model.addAttribute("userCreated", user.getCreatedAt());
                model.addAttribute("userRoles", user.getRoles());
                
                // Fetch user's registered conferences
                var registrations = registrationService.getMyConferences(email);
                model.addAttribute("registrations", registrations);
            }
        }
        return "user/profile";
    // Removed duplicate getRegisterPage method from merge conflict
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "public/about";
    }

    @GetMapping("/contact")
    public String getContactPage() {
        return "public/contact";
    }

    /* ===================== REGISTER ===================== */
    @GetMapping("/register")
    public String getRegister() {
        return "public/register";
    }

    @GetMapping("/schedule-attendance")
    public String mySchedule(Authentication auth, Model model) {

        String email = auth.getName();

        model.addAttribute(
            "schedules",
            sessionAttendanceService.getMySchedule(email)
        );

        return "user/user-schedule";

    }
    
    /* ===================== MY CONFERENCES (FIXED) ===================== */

    @GetMapping("/my-conferences")
    public String myConferences(Model model) {

        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        String email = auth.getName();

        List<Registration> registrations =
                registrationService.getMyRegistrationsByEmail(email);

        model.addAttribute("registrations", registrations);

        return "user/my-conferences";
    }

    @GetMapping("/home")
    public String getHome(Authentication auth,Model model) {
        System.out.println(
            "this is user principle"+
        SecurityContextHolder.getContext().getAuthentication().getDetails()

        );
        model.addAttribute("mySessions",scheduleService.getMyUpcomingSessions(auth.getName()));

        // Include chaired sessions for users with the chair role
        boolean isChair = auth.getAuthorities().stream()
            .anyMatch(a -> "ROLE_CHAIR".equals(a.getAuthority()));

        if (isChair) {
            model.addAttribute("chairedSessions", chairService.getChairedSessions(auth.getName()));
        } else {
            model.addAttribute("chairedSessions", java.util.Collections.emptyList());
        }

        return "user/home";
    }

}

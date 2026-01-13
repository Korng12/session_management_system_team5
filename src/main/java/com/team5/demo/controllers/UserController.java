package com.team5.demo.controllers;

import com.team5.demo.dto.UserScheduleDto;
import com.team5.demo.entities.Registration;
import com.team5.demo.entities.Role;
import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.RoleRepository;
import com.team5.demo.repositories.UserRepository;
import com.team5.demo.services.RegistrationService;
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
    private com.team5.demo.services.ChairService chairService;

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

    @GetMapping("/register")
    public String getRegisterPage() {
        return "public/register";
    }

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


    @GetMapping("/home")
    public String getHome(Model model, Authentication auth) {
        String email = auth.getName();
        var mySessions = scheduleService.getMyUpcomingSessions(email);
        model.addAttribute("mySessions", mySessions);
        if(auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CHAIR"))){
            model.addAttribute(
                "chairedSessions",
                chairService.getChairedSessions(email)
            );
        }
        return "user/home";
    }


    /* ===================== MY CONFERENCES (FIXED) ===================== */

    @GetMapping("/my-conferences")
    public String getMyConferences(Authentication auth, Model model) {
        String email = auth.getName();
        var reg = registrationService.getMyConferences(email);
        model.addAttribute("registrations", reg);
        return "user/my-conferences";
    }

    @GetMapping("/schedule-attendance")
    public String mySchedule(Authentication auth, Model model) {

        String email = auth.getName();

        List<UserScheduleDto> sessions = scheduleService.getUserSchedule(email);
        
        // Calculate attendance statistics
        long presentCount = sessions.stream()
                .filter(s -> s.getAttendance() != null && s.getAttendance().name().equals("PRESENT"))
                .count();
        long absentCount = sessions.stream()
                .filter(s -> s.getAttendance() != null && s.getAttendance().name().equals("ABSENT"))
                .count();
        long totalSessions = sessions.size();
        long pendingCount = totalSessions - presentCount - absentCount;

        model.addAttribute("sessions", sessions);
        model.addAttribute("presentCount", presentCount);
        model.addAttribute("absentCount", absentCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("totalSessions", totalSessions);

        return "user/user-schedule";

    }
    
}

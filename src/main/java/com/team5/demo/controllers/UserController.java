package com.team5.demo.controllers;

import com.team5.demo.dto.UserScheduleDto;
import com.team5.demo.entities.Registration;
import com.team5.demo.entities.Role;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.RoleRepository;
import com.team5.demo.repositories.UserRepository;
import com.team5.demo.services.RegistrationService;
import com.team5.demo.services.ScheduleService;

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

    /**
     * Handles user-related views and basic authentication flows
     * This controller manages:
     * - Landing/registration pages
     * - User account views
     * - Profile management endpoints
     */
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

    @GetMapping("/")
    public String getLandingPage() {
        return "public/index";
    }

    @GetMapping("/register")
    public String getRegisterPage() {
        return "public/register";
    }

    @GetMapping("/registeration")
    public String getRegisterationPage() {
        return "public/registeration";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam(required = false) String telegram,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpServletRequest request,
            Model model) {

        try {
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                return "public/register";
            }

            if (userRepository.findByEmail(email).isPresent()) {
                model.addAttribute("error", "Email already exists");
                return "public/register";
            }

            User user = new User();
            user.setName(firstName + " " + lastName);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));

            Role attendeeRole = roleRepository.findByName("ATTENDEE")
                    .orElseThrow(() -> new RuntimeException("Role ATTENDEE not found"));
            user.getRoles().add(attendeeRole);

            userRepository.save(user);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);
            Authentication authentication = authenticationManager.authenticate(authToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            request.getSession().setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext());

            return "redirect:/home";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "public/register";
        }
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "public/login";
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


    @GetMapping("/schedule-attendance")
    public String mySchedule(Authentication authentication, Model model) {

        String email = authentication.getName();

        List<UserScheduleDto> schedule =
                scheduleService.getUserSchedule(email);

        model.addAttribute("sessions", schedule);

        return "user/user-schedule";
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
    }

    @GetMapping("/about")
    public String getAboutPage() {
        return "public/about";
    }

    @GetMapping("/contact")
    public String getContactPage() {
        return "public/contact";
    }
    @GetMapping("/my-conferences")
    public String getMyConferences(Authentication auth,Model model) {
        String email =auth.getName();
        var reg =registrationService.getMyConferences(email);
        model.addAttribute("registrations", reg);
        return "user/my-conferences";
    }
    
}
=======
=======
    
    @PostMapping("/register")
    public String registerUser(
            package com.team5.demo.controllers;

            import com.team5.demo.entities.Role;
            import com.team5.demo.entities.User;
            import com.team5.demo.repositories.RoleRepository;
            import com.team5.demo.repositories.UserRepository;
            import jakarta.servlet.http.HttpServletRequest;
            import java.security.Principal;
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

                @GetMapping("/")
                public String getLandingPage() {
                    return "public/index";
                }

                @GetMapping("/register")
                public String getRegisterPage() {
                    return "public/register";
                }

                @GetMapping("/registeration")
                public String getRegisterationPage() {
                    return "public/registeration";
                }

                @PostMapping("/register")
                public String registerUser(
                        @RequestParam String firstName,
                        @RequestParam String lastName,
                        @RequestParam String email,
                        @RequestParam(required = false) String telegram,
                        @RequestParam String password,
                        @RequestParam String confirmPassword,
                        HttpServletRequest request,
                        Model model) {

                    try {
                        if (!password.equals(confirmPassword)) {
                            model.addAttribute("error", "Passwords do not match");
                            return "public/register";
                        }

                        if (userRepository.findByEmail(email).isPresent()) {
                            model.addAttribute("error", "Email already exists");
                            return "public/register";
                        }

                        User user = new User();
                        user.setName(firstName + " " + lastName);
                        user.setEmail(email);
                        user.setPassword(passwordEncoder.encode(password));

                        Role attendeeRole = roleRepository.findByName("ATTENDEE")
                                .orElseThrow(() -> new RuntimeException("Role ATTENDEE not found"));
                        user.getRoles().add(attendeeRole);

                        userRepository.save(user);

                        UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(email, password);
                        Authentication authentication = authenticationManager.authenticate(authToken);
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        request.getSession().setAttribute(
                            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                            SecurityContextHolder.getContext()
                        );

                        return "redirect:/home";
                    } catch (Exception e) {
                        model.addAttribute("error", "Registration failed: " + e.getMessage());
                        return "public/register";
                    }
                }

                @GetMapping("/login")
                public String getLoginPage() {
                    return "public/login";
                }

                @GetMapping("/home")
                public String getHome() {
                    return "user/home";
                }

                @GetMapping("/schedule")
                public String getSchedulePage() {
                    return "user/user-schedule";
                }

                @GetMapping("/register-conference")
                public String getRegisterConferencePage() {
                    return "public/registration";
                }

                @GetMapping("/conferences")
                public String getConferencesPage() {
                    return "user/conferences";
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
                        }
                    }
                    return "user/profile";
                }

                @GetMapping("/about")
                public String getAboutPage() {
                    return "public/aboutPage";
                }

                @GetMapping("/contact")
                public String getContactPage() {
                    return "public/contact";
                }
            }
>>>>>>> ee4982f (feat: Add profile endpoint, auto-login registration, security config updates, and view templates)

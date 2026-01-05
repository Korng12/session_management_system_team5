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
        return "public/about";
    }

    @GetMapping("/contact")
    public String getContactPage() {
        return "public/contact";
    }
}

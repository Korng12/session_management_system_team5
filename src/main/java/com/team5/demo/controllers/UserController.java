package com.team5.demo.controllers;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {
    
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
    @GetMapping("/login")
    public String getLoginPage() {
        return "public/login";
    }
    @GetMapping("/home")
    public String getHome() {
    //         boolean isAdmin = authentication.getAuthorities().stream()
    //     .anyMatch(a -> a.getAuthority().equals("ADMIN"));

    // model.addAttribute("isAdmin", isAdmin);
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
    @GetMapping("/about")
    public String getAboutPage() {
        return "public/aboutPage";
    }
       @GetMapping("/contact")
    public String getContactPage() {
        return "public/contact";
    }
    @GetMapping("/user-profile")
    public String getUserProfile() {
        return "user/profile";
    }
    
    
    
}
package com.team5.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;




@Controller
public class UserController {

    @GetMapping("/")
    public String showHomePage() {
        return "user/index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "user/UserRegister";
    }

    @GetMapping("/schedule")
    public String showSchedule(Model model) {
        return "schedule";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "user/login";
    }

    @GetMapping("/profile")
    public String showProfile(Model model) {
        return "user/profile";
    }

    @GetMapping("/my-schedule")
    public String showMySchedule(Model model) {
        return "schedule";
    }

    @GetMapping("/user/dashboard")
    public String showUserDashboard(Model model) {
        return "user/dashboard";
    }

    @PostMapping("/register")
    public String registerUser(Model model) {
        // Just return the form for UI display
        return "user/UserRegister";
    }

}


package com.team5.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    // User Homepage - Index
    @GetMapping("/")
    public String showHomepage(Model model) {
        return "public/index"; // Main landing page
    }

    // Login Page
    @GetMapping("/login")
    public String showLoginPage(Model model) {
        return "public/login"; // User login view
    }

    // Registration Page
    @GetMapping("/register")
    public String showRegistrationPage(Model model) {
        return "public/register"; // User registration view
    }

    // Conferences Page
    @GetMapping("/user/conferences")
    public String showConferencesPage(Model model) {
        return "user/conferences"; // View available conferences
    }

    // User Schedule Page
    @GetMapping("/user/user-schedule")
    public String showUserSchedule(Model model) {
        return "user/user-schedule"; // User's schedule view
    }
    @GetMapping("/user/profile")
    public String showUserProfile(Model model) {
        return "user/profile"; // User's profile view
    }
}
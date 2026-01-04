package com.team5.demo.controllers;

import org.springframework.stereotype.Controller;
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
    @GetMapping("/about")
    public String getAboutPage() {
        return "public/about";
    }
    
}

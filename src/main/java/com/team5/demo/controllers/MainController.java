package com.team5.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/admin/sessions")
    public String manageSessions() {
        return "admin/manage-sessions";
    }

    @GetMapping("/admin/rooms")
    public String manageRooms() {
        return "admin/manage-rooms";
    }
}

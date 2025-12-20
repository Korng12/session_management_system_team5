package com.team5.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class AdminController {

    @GetMapping("/admin")
    public String showAdminHomepage(Model model) {
        return "admin/homepageAdmin";
    }

    @GetMapping("/admin/sessions")
    public String manageSessions(Model model) {
        return "admin/sessionManagement";
    }
}
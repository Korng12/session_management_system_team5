package com.team5.demo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String showHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        return "UserRegister";
    }

    @PostMapping("/register")
    public String registerUser(Model model) {
        // Just return the form for UI display
        return "UserRegister";
    }

}


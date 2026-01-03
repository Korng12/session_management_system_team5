package com.team5.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
 @RequestMapping("/user")
public class UserController {
      @GetMapping("/homepage") // Adjusted endpoint
    public String homePageUser() {
        return "user/homepage"; // Ensure this points to the correct Thymeleaf template
    }

    @GetMapping("/my_schedule")
    public String mySchedule() {
        return "user/user-schedule";
    }

    @GetMapping("/register")
    public String register() {
        return "public/register";
    }

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }
  
}
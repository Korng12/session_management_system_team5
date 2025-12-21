package com.team5.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ParticipantController {
    @GetMapping("/")
    public String login(Model model) {
        model.addAttribute("title","This is login form");
        model.addAttribute("content","fragments/login");
        return ("public/index");
    }
    @GetMapping("/register")
    public String getMethodName(Model model) {
        model.addAttribute("title","This is register form");
        model.addAttribute("content","fragments/register");
        return ("public/index");
    }
    


}

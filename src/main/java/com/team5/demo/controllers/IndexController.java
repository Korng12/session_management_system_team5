package com.team5.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class IndexController {
    @GetMapping("/")
    public String index() {
        return "public/index"; // Points to the index.html template
    }
}

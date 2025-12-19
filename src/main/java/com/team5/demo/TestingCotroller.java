package com.team5.demo;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class TestingCotroller {
  @GetMapping("/login")
  public String getMethodName() {
      return new String("this is team5, session management system");
  }
  
}

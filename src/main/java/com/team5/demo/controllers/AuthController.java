package com.team5.demo.controllers;

import com.team5.demo.dto.AuthResponse;
import com.team5.demo.dto.LoginRequest;
import com.team5.demo.dto.RegisterRequest;
import com.team5.demo.entities.Role;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.RoleRepository;
import com.team5.demo.repositories.UserRepository;
import com.team5.demo.security.JwtUtil;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
            JwtUtil jwtUtil,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // helper for validation errors
    private Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

    // @PostMapping("/register")
    // public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    // if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    // return ResponseEntity.badRequest().body("Email already exists");
    // }

    // User user = new User();
    // user.setName(request.getName());
    // user.setEmail(request.getEmail());
    // user.setPassword(passwordEncoder.encode(request.getPassword()));

    // Role attendeeRole = roleRepository.findByName("ATTENDEE")
    // .orElseThrow(() -> new RuntimeException("Role ATTENDEE not found"));
    // user.addRole(attendeeRole);

    // userRepository.save(user);

    // String token = jwtUtil.generateToken(user.getEmail());
    // return ResponseEntity.ok(new AuthResponse(user.getEmail(), token));
    // }
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult result,
            HttpServletResponse response) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(result));
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role attendeeRole = roleRepository.findByName("ATTENDEE")
                .orElseThrow(() -> new RuntimeException("Role ATTENDEE not found"));
        user.addRole(attendeeRole);

        userRepository.save(user);
        //  DEFAULT EXPIRY for register (1 hour)
        long jwtExpiryMillis = 60L * 60 * 1000; // 1 hour
        int cookieAge = 60 * 60; // 1 hour

        //  Generate JWT using new method
        String token = jwtUtil.generateToken(user.getEmail(), jwtExpiryMillis);

        //  Store JWT in HttpOnly cookie
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(cookieAge);
        response.addCookie(cookie);
        System.out.println("Login successful, JWT cookie set."+ cookie.getName());

        return ResponseEntity.ok().build();
    }

    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    // Authentication authentication = authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(request.getEmail(),
    // request.getPassword())
    // );

    // String token = jwtUtil.generateToken(request.getEmail());
    // return ResponseEntity.ok(new AuthResponse(request.getEmail(), token));
    // }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request,
            BindingResult result,
            HttpServletResponse response) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(getErrors(result));
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "message", "Invalid email or password"));
        }
        long jwtExpiryMillis = request.isRememberMe()
                ? 7L * 24 * 60 * 60 * 1000   // 7 days
                : 60L * 60 * 1000; // 1 hour

        String token = jwtUtil.generateToken(request.getEmail(), jwtExpiryMillis);

        // Cookie expiration depends on rememberMe
        int cookieAge = request.isRememberMe()
                ? 7 * 24 * 60 * 60// 7 days
                : 60 * 60; // 1 hour

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(cookieAge);
        response.addCookie(cookie);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);
        return ResponseEntity.ok().build();
    }
}
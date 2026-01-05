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

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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

    // @PostMapping("/register")
    // public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
    //     if (userRepository.findByEmail(request.getEmail()).isPresent()) {
    //         return ResponseEntity.badRequest().body("Email already exists");
    //     }

    //     User user = new User();
    //     user.setName(request.getName());
    //     user.setEmail(request.getEmail());
    //     user.setPassword(passwordEncoder.encode(request.getPassword()));

    //     Role attendeeRole = roleRepository.findByName("ATTENDEE")
    //             .orElseThrow(() -> new RuntimeException("Role ATTENDEE not found"));
    //     user.addRole(attendeeRole);

    //     userRepository.save(user);

    //     String token = jwtUtil.generateToken(user.getEmail());
    //     return ResponseEntity.ok(new AuthResponse(user.getEmail(), token));
    // }
    @PostMapping("/register")
public ResponseEntity<?> register(
        @RequestBody RegisterRequest request,
        HttpServletResponse response) {

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

    // 🔐 Generate JWT
    String token = jwtUtil.generateToken(user.getEmail());

    // 🍪 Store JWT in HttpOnly cookie
    Cookie cookie = new Cookie("jwt", token);
    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(60 * 60); // 1 hour
    response.addCookie(cookie);

    return ResponseEntity.ok().build();
}


    // @PostMapping("/login")
    // public ResponseEntity<?> login(@RequestBody LoginRequest request) {
    //     Authentication authentication = authenticationManager.authenticate(
    //             new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
    //     );

    //     String token = jwtUtil.generateToken(request.getEmail());
    //     return ResponseEntity.ok(new AuthResponse(request.getEmail(), token));
    // }
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response) {

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );
        System.out.println("Authentication successful for user: " + auth.getPrincipal()+" "+ auth.getAuthorities()+ "" + auth.isAuthenticated());

        String token = jwtUtil.generateToken(request.getEmail());

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60); // 1 hour
        response.addCookie(cookie);
        System.out.println("Login successful, JWT cookie set."+ cookie.getName());

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
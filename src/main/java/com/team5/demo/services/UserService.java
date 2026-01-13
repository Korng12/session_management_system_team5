package com.team5.demo.services;

import com.team5.demo.entities.Role;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.RoleRepository;
import com.team5.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Long getCurrentUserId() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName(); // username/email

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getId();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User create(String name, String email, String rawPassword, String roleName) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        User u = new User();
        u.setName(name);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.getRoles().clear();
        u.getRoles().add(role);

        return userRepository.save(u);
    }

    public User update(Long id, String name, String email, String newPasswordOrBlank, String roleName) {
        User u = findById(id);

        // email change check
        if (!u.getEmail().equalsIgnoreCase(email) && userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));

        u.setName(name);
        u.setEmail(email);

        // only update password if admin typed one
        if (newPasswordOrBlank != null && !newPasswordOrBlank.isBlank()) {
            u.setPassword(passwordEncoder.encode(newPasswordOrBlank));
        }

        u.getRoles().clear();
        u.getRoles().add(role);

        return userRepository.save(u);
    }

public void delete(Long id, String currentUserEmail) {
    User target = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    boolean isAdmin = target.getRoles().stream()
        .anyMatch(r -> "ADMIN".equalsIgnoreCase(r.getName()));

    // Optional: block self delete
    if (target.getEmail().equalsIgnoreCase(currentUserEmail)) {
        throw new IllegalStateException("You cannot delete your own account");
    }

    // Block deleting the last admin
    if (isAdmin) {
        long adminCount = userRepository.countByRoles_Name("ADMIN");
        if (adminCount <= 1) {
            throw new IllegalStateException("You cannot delete the last admin account");
        }
    }

    userRepository.delete(target);
    }
}

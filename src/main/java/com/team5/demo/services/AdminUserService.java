package com.team5.demo.services;

import com.team5.demo.dto.AdminCreateUserRequest;
import com.team5.demo.dto.AdminUpdateUserRequest;
import com.team5.demo.dto.AdminUserDTO;
import com.team5.demo.entities.Role;
import com.team5.demo.entities.User;
import com.team5.demo.repositories.RoleRepository;
import com.team5.demo.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminUserService(UserRepository userRepository,
                            RoleRepository roleRepository,
                            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ CREATE USER
    public AdminUserDTO createUser(AdminCreateUserRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setName(req.getName());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));

        user.setRoles(resolveRoles(req.getRoles()));

        return toDTO(userRepository.save(user));
    }

    // ✅ READ ALL USERS
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    // ✅ READ USER BY ID
    public AdminUserDTO getUserById(Long id) {
        return toDTO(getUser(id));
    }

    // ✅ UPDATE USER
    public AdminUserDTO updateUser(Long id, AdminUpdateUserRequest req) {
        User user = getUser(id);

        if (req.getName() != null) user.setName(req.getName());
        if (req.getEmail() != null) user.setEmail(req.getEmail());

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }

        if (req.getRoles() != null) {
            user.setRoles(resolveRoles(req.getRoles()));
        }

        return toDTO(userRepository.save(user));
    }

    // ✅ DELETE USER
    public void deleteUser(Long id) {
        userRepository.delete(getUser(id));
    }

    // ✅ ASSIGN SINGLE ROLE
    public AdminUserDTO addRole(Long userId, String roleName) {
        User user = getUser(userId);
        Role role = getRole(roleName);
        user.getRoles().add(role);
        return toDTO(userRepository.save(user));
    }

    // ✅ REMOVE SINGLE ROLE
    public AdminUserDTO removeRole(Long userId, String roleName) {
        User user = getUser(userId);
        user.getRoles().removeIf(r -> r.getName().equalsIgnoreCase(roleName));
        return toDTO(userRepository.save(user));
    }

    // ✅ REPLACE ALL ROLES
    public AdminUserDTO replaceRoles(Long userId, Set<String> roles) {
        User user = getUser(userId);
        user.setRoles(resolveRoles(roles));
        return toDTO(userRepository.save(user));
    }

    // --------------------- Helper Methods ---------------------

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private Role getRole(String roleName) {
        return roleRepository.findByName(roleName.toUpperCase())
                .orElseThrow(() -> new RuntimeException("Role not found: " + roleName));
    }

    private Set<Role> resolveRoles(Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) {
            return Set.of(getRole("ATTENDEE")); // default
        }
        return roleNames.stream().map(this::getRole).collect(Collectors.toSet());
    }

    private AdminUserDTO toDTO(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());

        return new AdminUserDTO(user.getId(), user.getName(), user.getEmail(), roles);
    }
}

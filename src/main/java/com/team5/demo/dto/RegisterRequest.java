package com.team5.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterRequest {
 @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9-]+\\.(com|net|org|edu|gov|io|co|kh)$", message = "Email must end with: com, net, org, edu, gov, io, co, kh")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$", message = "Password must contain at least 1 letter and 1 number")
    private String password;

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 50, message = "Name must be 3 - 50 characters")
    private String name;


    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}

package com.team5.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        String password = "testpass123";
        String hash = encoder.encode(password);
        System.out.println("Hash for 'testpass123': " + hash);
        System.out.println("Verification: " + encoder.matches(password, hash));
    }
}

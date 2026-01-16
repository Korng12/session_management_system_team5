package com.team5.demo;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptTest {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(10);
        LocalDateTime now = LocalDateTime.now();   
        System.out.println("Current Date and Time: " + now  );
        String password = "testpass123";
        String hash = encoder.encode(password);
        System.out.println("Hash for 'testpass123': " + hash);
        System.out.println("Verification: " + encoder.matches(password, hash));
        System.out.println(">>> JVM now = " + LocalDateTime.now());
        System.out.println(">>> JVM zone = " + ZoneId.systemDefault());

    }
}

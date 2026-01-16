package com.team5.demo;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		        System.out.println(">>> JVM now = " + LocalDateTime.now());
System.out.println(">>> JVM zone = " + ZoneId.systemDefault());

	}

}

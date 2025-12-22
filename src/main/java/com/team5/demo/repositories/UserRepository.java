package com.team5.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.team5.demo.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

	
}
package com.team5.demo.repositories;

import com.team5.demo.dto.AdminRegistrationView;
import com.team5.demo.entities.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRegistrationViewRepository
        extends JpaRepository<Registration, Long> {

    @Query("""
        SELECT new com.team5.demo.dto.AdminRegistrationView(
            r.id,
            u.email,
            c.title,
            s.title,
            r.status
        )
        FROM Registration r
        JOIN r.participant u
        JOIN r.conference c
        LEFT JOIN SessionAttendance sa
            ON sa.participant = u
        LEFT JOIN sa.session s
            ON s.conference = c
    """)
    List<AdminRegistrationView> findAdminRegistrationView();
}

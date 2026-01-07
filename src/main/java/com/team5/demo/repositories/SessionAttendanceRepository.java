package com.team5.demo.repositories;

import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.entities.SessionAttendanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionAttendanceRepository
        extends JpaRepository<SessionAttendance, SessionAttendanceId> {

    //  Load attendance with user + session
    @Query("""
        SELECT a FROM SessionAttendance a
        JOIN FETCH a.participant
        JOIN FETCH a.session
    """)
    List<SessionAttendance> findAllWithRelations();
}

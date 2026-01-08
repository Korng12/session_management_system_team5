package com.team5.demo.repositories;

import com.team5.demo.entities.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {

    // === BASIC ===
    Optional<Conference> findById(Long id);

    // === USER-FACING ===
    List<Conference> findByTitleContainingIgnoreCase(String title);

    List<Conference> findByStartDateAfter(LocalDate date);

    List<Conference> findByEndDateBefore(LocalDate date);

    // === UPCOMING / ONGOING CONFERENCES ===
    @Query("""
        SELECT c FROM Conference c
        WHERE c.endDate >= :today
        ORDER BY c.startDate ASC
    """)
    List<Conference> findUpcomingConferences(@Param("today") LocalDate today);
}

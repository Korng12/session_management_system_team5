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

  
    Optional<Conference> findById(Long id);

    List<Conference> findByTitleContainingIgnoreCase(String title);

    

    List<Conference> findByStartDateAfter(LocalDate date);

    List<Conference> findByEndDateBefore(LocalDate date);

    @Query("""
        SELECT c FROM Conference c
        WHERE c.endDate >= :today
        ORDER BY c.startDate ASC
    """)
    List<Conference> findUpcomingConferences(@Param("today") LocalDate today);
}

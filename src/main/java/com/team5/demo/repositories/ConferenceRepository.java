package com.team5.demo.repositories;

import com.team5.demo.entities.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface ConferenceRepository extends JpaRepository<Conference, Long> {
    List<Conference> findByTitleContaining(String title);
    List<Conference> findByStartDateAfter(LocalDate date);
    List<Conference> findByEndDateBefore(LocalDate date);
}
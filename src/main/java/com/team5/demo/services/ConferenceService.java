package com.team5.demo.services;

import com.team5.demo.entities.Conference;
import com.team5.demo.repositories.ConferenceRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


// @RequiredArgsConstructor
@Service
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;

    public ConferenceService(ConferenceRepository conferenceRepository) {
        this.conferenceRepository = conferenceRepository;
    }

    public List<Conference> getAllConferences() {
        return conferenceRepository.findAll();
    }

    public Conference getById(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
    }

    public Conference save(Conference conference) {
        return conferenceRepository.save(conference);
    }

    @Transactional
    public void delete(Long id) {
        Conference conf = conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));

        if ((conf.getSessions() != null && !conf.getSessions().isEmpty()) ||
            (conf.getRegistrations() != null && !conf.getRegistrations().isEmpty())) {
            throw new RuntimeException("Cannot delete conference with related data");
        }

        conferenceRepository.delete(conf);
    }

    
    public Conference getConferenceById(Long id) {
    return conferenceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conference not found"));
    }




 

    /**
     * Get upcoming or ongoing conferences (user-facing)
     */
    public List<Conference> getAvailableConferences() {
        return conferenceRepository.findUpcomingConferences(LocalDate.now());
    }

    /**
     * Get conference by id (used for detail page)
     */
    public Conference getConference(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conference not found"));
    }
}

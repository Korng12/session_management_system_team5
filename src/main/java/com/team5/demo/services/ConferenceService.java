package com.team5.demo.services;

import com.team5.demo.entities.Conference;
import com.team5.demo.repositories.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConferenceService {
    private final ConferenceRepository conferenceRepository;

    public List<Conference> getAllConferences() {
        return conferenceRepository.findAll();
    }

    // Manage Conference
    public Conference save(Conference conference) {

        if (conference.getStartDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                "Start date cannot be in the past."
            );
        }

        if (conference.getEndDate().isBefore(conference.getStartDate())) {
            throw new IllegalArgumentException(
                "End date must be after start date."
            );
        }

        return conferenceRepository.save(conference);
    }


    
    public Conference getById(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conference not found"));
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

    // This function for sum total in admin
    public long countAll() {
       return conferenceRepository.count();
    }

    // search for conference 
    public List<Conference> searchByTitle(String keyword) {
        return conferenceRepository.findByTitleContainingIgnoreCase(keyword);
    }

   
 

    /**
     * Get upcoming or ongoing conferences (user-facing)
     */
    public List<Conference> getAvailableConferences() {
        return conferenceRepository.findUpcomingConferences(LocalDate.now());
    }

    /**
     * Return all conferences (used for listing where completed ones should be visible)
     */
    @Transactional(readOnly = true)
    public List<Conference> getAllConferencesReadOnly() {
        return conferenceRepository.findAll();
    }

    /**
     * Get conference by id (used for detail page)
     */
    public Conference getConference(Long id) {
        return conferenceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Conference not found"));
    }

    public List<Conference> getConferencesByTittle(String title) {
        return conferenceRepository.findByTitleContainingIgnoreCase(title);
    }

    public Object findAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findAll'");
    }
}


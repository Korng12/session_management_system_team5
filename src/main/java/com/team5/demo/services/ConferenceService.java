package com.team5.demo.services;

import com.team5.demo.entities.Conference;
import com.team5.demo.repositories.ConferenceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConferenceService {

    private final ConferenceRepository conferenceRepository;

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

    public void delete(Long id) {
    Conference conf = conferenceRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Conference not found"));

    if (!conf.getSessions().isEmpty() || !conf.getRegistrations().isEmpty()) {
        throw new RuntimeException("Cannot delete conference because it has related data");
    }

    conferenceRepository.delete(conf);
}

}

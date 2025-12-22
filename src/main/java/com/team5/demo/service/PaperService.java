package com.team5.demo.service;

import com.team5.demo.entities.Paper;
import com.team5.demo.entities.Session;
import com.team5.demo.entities.PaperStatus;
import com.team5.demo.exception.DuplicateResourceException;
import com.team5.demo.exception.ResourceNotFoundException;
import com.team5.demo.repositories.PaperRepository;
import com.team5.demo.repositories.SessionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PaperService {
    
    @Autowired
    private PaperRepository paperRepository;
    
    @Autowired
    private SessionRepository sessionRepository;
    
    public List<Paper> getAllPapers() {
        return paperRepository.findAll();
    }
    
    public Paper getPaperById(@NonNull Long id) {
        return paperRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paper not found with id: " + id));
    }
    
    public Paper createPaper(Paper paper) {
        // Validate required fields
        if (paper.getTitle() == null || paper.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Paper title cannot be empty");
        }
        
        if (paper.getAuthorNames() == null || paper.getAuthorNames().trim().isEmpty()) {
            throw new IllegalArgumentException("Author names cannot be empty");
        }
        
        if (paper.getAbstractText() == null || paper.getAbstractText().trim().isEmpty()) {
            throw new IllegalArgumentException("Abstract cannot be empty");
        }
        
        // Set submission date if not provided
        if (paper.getSubmissionDate() == null) {
            paper.setSubmissionDate(LocalDateTime.now());
        }
        
        // Check for duplicate paper
        if (paperRepository.existsByTitleAndAuthorNames(paper.getTitle(), paper.getAuthorNames())) {
            throw new DuplicateResourceException("Paper with this title and author already exists");
        }
        
        return paperRepository.save(paper);
    }
    
    public Paper updatePaper(Long id, Paper paperDetails) {
        Paper paper = getPaperById(id);
        
        paper.setTitle(paperDetails.getTitle());
        paper.setAbstractText(paperDetails.getAbstractText());
        paper.setAuthorNames(paperDetails.getAuthorNames());
        paper.setFilePath(paperDetails.getFilePath());
        paper.setStatus(paperDetails.getStatus());
        
        if (paperDetails.getSession() != null) {
            // Validate session exists
            Session session = sessionRepository.findById(paperDetails.getSession().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Session not found"));
            paper.setSession(session);
        }
        
        paper.setPresentationOrder(paperDetails.getPresentationOrder());
        
        return paperRepository.save(paper);
    }
    
    public void deletePaper(Long id) {
        Paper paper = getPaperById(id);
        paperRepository.delete(paper);
    }
    
    public List<Paper> getPapersByStatus(PaperStatus status) {
        return paperRepository.findByStatus(status);
    }
    
    public List<Paper> getPapersBySession(Long sessionId) {
        return paperRepository.findBySessionId(sessionId);
    }
    
    public List<Paper> searchPapersByAuthor(String authorName) {
        return paperRepository.findByAuthorNamesContaining(authorName);
    }
    
    public List<Paper> searchPapersByTitle(String title) {
        return paperRepository.findByTitleContainingIgnoreCase(title);
    }
    
    public List<Paper> getPapersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paperRepository.findBySubmissionDateBetween(startDate, endDate);
    }
    
    public Paper assignPaperToSession(Long paperId, Long sessionId) {
        Paper paper = getPaperById(paperId);
        Session session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> new ResourceNotFoundException("Session not found with id: " + sessionId));
        
        paper.setSession(session);
        paper.setStatus(PaperStatus.SCHEDULED_FOR_PRESENTATION);
        
        return paperRepository.save(paper);
    }
    
    public Paper removePaperFromSession(Long paperId) {
        Paper paper = getPaperById(paperId);
        paper.setSession(null);
        paper.setPresentationOrder(null);
        paper.setStatus(PaperStatus.ACCEPTED);
        
        return paperRepository.save(paper);
    }
    
    public Paper updatePaperStatus(Long id, PaperStatus status) {
        Paper paper = getPaperById(id);
        paper.setStatus(status);
        return paperRepository.save(paper);
    }
    
    public long getPaperCountByStatus(PaperStatus status) {
        return paperRepository.countByStatus(status);
    }
    
    public List<Paper> getUnassignedAcceptedPapers() {
        return paperRepository.findUnassignedPapersByStatus(PaperStatus.ACCEPTED);
    }
}

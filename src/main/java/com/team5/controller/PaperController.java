package com.team5.controller;

import com.team5.entity.Paper;
import com.team5.entity.Paper.PaperStatus;
import com.team5.service.PaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/papers")
public class PaperController {
    
    @Autowired
    private PaperService paperService;
    
    @GetMapping
    public List<Paper> getAllPapers() {
        return paperService.getAllPapers();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Paper> getPaperById(@PathVariable Long id) {
        return ResponseEntity.ok(paperService.getPaperById(id));
    }
    
    @PostMapping
    public Paper createPaper(@RequestBody Paper paper) {
        return paperService.createPaper(paper);
    }
    
    @PutMapping("/{id}")
    public Paper updatePaper(@PathVariable Long id, @RequestBody Paper paperDetails) {
        return paperService.updatePaper(id, paperDetails);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePaper(@PathVariable Long id) {
        paperService.deletePaper(id);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/status/{status}")
    public List<Paper> getPapersByStatus(@PathVariable String status) {
        return paperService.getPapersByStatus(PaperStatus.valueOf(status.toUpperCase()));
    }
    
    @GetMapping("/session/{sessionId}")
    public List<Paper> getPapersBySession(@PathVariable Long sessionId) {
        return paperService.getPapersBySession(sessionId);
    }
    
    @GetMapping("/search/author")
    public List<Paper> searchPapersByAuthor(@RequestParam String author) {
        return paperService.searchPapersByAuthor(author);
    }
    
    @GetMapping("/search/title")
    public List<Paper> searchPapersByTitle(@RequestParam String title) {
        return paperService.searchPapersByTitle(title);
    }
    
    @GetMapping("/search/date-range")
    public List<Paper> getPapersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return paperService.getPapersByDateRange(startDate, endDate);
    }
    
    @PostMapping("/{paperId}/assign-session/{sessionId}")
    public Paper assignPaperToSession(@PathVariable Long paperId, @PathVariable Long sessionId) {
        return paperService.assignPaperToSession(paperId, sessionId);
    }
    
    @PostMapping("/{paperId}/remove-session")
    public Paper removePaperFromSession(@PathVariable Long paperId) {
        return paperService.removePaperFromSession(paperId);
    }
    
    @PatchMapping("/{id}/status")
    public Paper updatePaperStatus(@PathVariable Long id, @RequestParam String status) {
        return paperService.updatePaperStatus(id, PaperStatus.valueOf(status.toUpperCase()));
    }
    
    @GetMapping("/count/status/{status}")
    public long getPaperCountByStatus(@PathVariable String status) {
        return paperService.getPaperCountByStatus(PaperStatus.valueOf(status.toUpperCase()));
    }
    
    @GetMapping("/unassigned/accepted")
    public List<Paper> getUnassignedAcceptedPapers() {
        return paperService.getUnassignedAcceptedPapers();
    }
}

package com.team5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Entity
@Table(name = "papers")
public class Paper {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 200, message = "Title must be between 1 and 200 characters")
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "Abstract is required")
    @Size(min = 50, message = "Abstract must be at least 50 characters")
    @Column(columnDefinition = "TEXT")
    private String abstractText;

    @NotBlank(message = "Author names are required")
    @Column(name = "author_names", nullable = false)
    private String authorNames;

    @NotNull(message = "Submission date is required")
    @PastOrPresent(message = "Submission date cannot be in the future")
    @Column(name = "submission_date", nullable = false)
    private LocalDateTime submissionDate;

    @Size(max = 500, message = "File path cannot exceed 500 characters")
    @Column(name = "file_path")
    private String filePath;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaperStatus status = PaperStatus.SUBMITTED;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;

    @Min(value = 1, message = "Presentation order must be at least 1")
    @Column(name = "presentation_order")
    private Integer presentationOrder;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAbstractText() { return abstractText; }
    public void setAbstractText(String abstractText) { this.abstractText = abstractText; }

    public String getAuthorNames() { return authorNames; }
    public void setAuthorNames(String authorNames) { this.authorNames = authorNames; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public PaperStatus getStatus() { return status; }
    public void setStatus(PaperStatus status) { this.status = status; }

    public Session getSession() { return session; }
    public void setSession(Session session) { this.session = session; }

    public Integer getPresentationOrder() { return presentationOrder; }
    public void setPresentationOrder(Integer presentationOrder) { this.presentationOrder = presentationOrder; }
}

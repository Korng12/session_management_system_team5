package com.team5.demo.dto;

import jakarta.validation.constraints.*;
import com.team5.demo.validation.ValidSessionTime;
import com.team5.demo.entities.SessionStatus;
import java.time.LocalDateTime;

@ValidSessionTime
public class CreateSessionRequest {
    @NotBlank(message = "Session title is required")
    @Size(min = 3, max = 100, message = "Session title must be between 3 and 100 characters")
    private String title;

    @Min(value = 1, message = "Invalid chair ID")
    private Long chairId;

    @Min(value = 1, message = "Invalid room ID")
    private Long roomId;

    @NotNull(message = "Conference ID is required")
    @Min(value = 1, message = "Invalid conference ID")
    private Long conferenceId;

    @NotNull(message = "Start time is required")
    @FutureOrPresent(message = "Start time must be in the present or future")
    private LocalDateTime startTime;

    @NotNull(message = "End time is required")
    @FutureOrPresent(message = "End time must be in the present or future")
    private LocalDateTime endTime;

    private SessionStatus status;

    // Constructors
    public CreateSessionRequest() {
    }

    public CreateSessionRequest(String title, Long chairId, Long roomId, Long conferenceId, 
                               LocalDateTime startTime, LocalDateTime endTime) {
        this.title = title;
        this.chairId = chairId;
        this.roomId = roomId;
        this.conferenceId = conferenceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = SessionStatus.SCHEDULED;
    }

    public CreateSessionRequest(String title, Long chairId, Long roomId, Long conferenceId, 
                               LocalDateTime startTime, LocalDateTime endTime, SessionStatus status) {
        this.title = title;
        this.chairId = chairId;
        this.roomId = roomId;
        this.conferenceId = conferenceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getChairId() {
        return chairId;
    }

    public void setChairId(Long chairId) {
        this.chairId = chairId;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Integer getConferenceId() {
        return conferenceId;
    }

    public void setConferenceId(Long conferenceId) {
        this.conferenceId = conferenceId;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}

package com.team5.demo.dto;

import com.team5.demo.entities.SessionStatus;
import java.time.LocalDateTime;

public class SessionResponse {
    private Integer id;
    private String title;
    private String chairName;
    private String roomName;
    private String conferenceName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private SessionStatus status;

    // Constructors
    public SessionResponse() {
    }

    public SessionResponse(Integer id, String title, String chairName, String roomName, 
                         String conferenceName, LocalDateTime startTime, LocalDateTime endTime, 
                         LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.chairName = chairName;
        this.roomName = roomName;
        this.conferenceName = conferenceName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
    }

    public SessionResponse(Integer id, String title, String chairName, String roomName, 
                         String conferenceName, LocalDateTime startTime, LocalDateTime endTime, 
                         LocalDateTime createdAt, SessionStatus status) {
        this(id, title, chairName, roomName, conferenceName, startTime, endTime, createdAt);
        this.status = status;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChairName() {
        return chairName;
    }

    public void setChairName(String chairName) {
        this.chairName = chairName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public SessionStatus getStatus() {
        return status;
    }

    public void setStatus(SessionStatus status) {
        this.status = status;
    }
}

package com.team5.demo.dto;

import com.team5.demo.entities.SessionStatus;
import java.time.LocalDateTime;

public class SessionResponse {
    private Long id;
    private String title;
    private String chairName;
    private String roomName;
    private String conferenceName;
    private Long chairId;
    private Long roomId;
    private Integer roomCapacity;
    private Long conferenceId;
    private Integer totalRegistered;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    private SessionStatus status;
    private Integer version; // For optimistic locking
    private boolean deleted;
    private boolean full;

    // Constructors
    public SessionResponse() {
    }

    public SessionResponse(Long id, String title, String chairName, String roomName, 
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

    public SessionResponse(Long id, String title, String chairName, String roomName, 
                         String conferenceName, LocalDateTime startTime, LocalDateTime endTime, 
                         LocalDateTime createdAt, SessionStatus status) {
        this(id, title, chairName, roomName, conferenceName, startTime, endTime, createdAt);
        this.status = status;
    }
    
    public SessionResponse(Long id, String title, String chairName, String roomName, 
                         String conferenceName, LocalDateTime startTime, LocalDateTime endTime, 
                         LocalDateTime createdAt, SessionStatus status, Integer version) {
        this(id, title, chairName, roomName, conferenceName, startTime, endTime, createdAt, status);
        this.version = version;
    }
    public boolean isFull() {
    return full;
}

public void setFull(boolean full) {
    this.full = full;
}


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    public void setRoomCapacity(Integer roomCapacity) {
        this.roomCapacity = roomCapacity;
    }
    public Integer getRoomCapacity() {
        return roomCapacity;
    }
    public Integer getTotalRegistered(){
        return totalRegistered;
    }
    public void setTotalRegistered(Integer totalRegistered){
        this.totalRegistered = totalRegistered;
    }   
    public String getConferenceName() {
        return conferenceName;
    }

    public void setConferenceName(String conferenceName) {
        this.conferenceName = conferenceName;
    }

    public Long getChairId() {
        return chairId;
    }

    public void setChairId(Long chairId) {
        this.chairId = chairId;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public Long getConferenceId() {
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
    
    public Integer getVersion() {
        return version;
    }
    
    public void setVersion(Integer version) {
        this.version = version;
    }
    
    public boolean isDeleted() {
        return deleted;
    }
    
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}

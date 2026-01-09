package com.team5.demo.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.team5.demo.entities.AttendanceStatus;
import com.team5.demo.entities.SessionStatus;

public class UserScheduleDto {

    private Long sessionId;
    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private SessionStatus sessionStatus;
    private AttendanceStatus attendanceStatus; // nullable
    public Long getSessionId() {
        return sessionId;
    }
    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }
    public void setSessionStatus(SessionStatus sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }
    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }
    public UserScheduleDto(Long sessionId, String title, LocalDate date, LocalTime startTime, LocalTime endTime,
            SessionStatus sessionStatus, AttendanceStatus attendanceStatus) {
        this.sessionId = sessionId;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionStatus = sessionStatus;
        this.attendanceStatus = attendanceStatus;
    }
    

    // constructor, getters
}

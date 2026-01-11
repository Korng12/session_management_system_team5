package com.team5.demo.dto;

import com.team5.demo.entities.AttendanceStatus;
import com.team5.demo.entities.SessionStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class UserScheduleDto {

    private String title;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private SessionStatus sessionStatus;
    private AttendanceStatus attendance;

    public UserScheduleDto(String title,
                           LocalDate date,
                           LocalTime startTime,
                           LocalTime endTime,
                           SessionStatus sessionStatus,
                           AttendanceStatus attendance) {
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionStatus = sessionStatus;
        this.attendance = attendance;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public SessionStatus getSessionStatus() {
        return sessionStatus;
    }

    public AttendanceStatus getAttendance() {
        return attendance;
    }
}

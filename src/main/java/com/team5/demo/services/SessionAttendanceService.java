package com.team5.demo.services;

import com.team5.demo.entities.SessionAttendance;
import com.team5.demo.repositories.SessionAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionAttendanceService {

    private final SessionAttendanceRepository attendanceRepository;

    /* ===================== ADMIN ===================== */

    @Transactional(readOnly = true)
    public List<SessionAttendance> getAllAttendances() {
        List<SessionAttendance> list =
                attendanceRepository.findAllWithRelations();

        System.out.println("ATTENDANCES FOUND = " + list.size());
        return list;
    }

    /* ===================== USER ===================== */

    @Transactional(readOnly = true)
    public List<SessionAttendance> getMySchedule(String email) {
        return attendanceRepository.findMySchedule(email);
    }
}

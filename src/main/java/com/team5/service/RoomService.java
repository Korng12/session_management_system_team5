package com.team5.demo.service;

import com.team5.demo.model.Room;  // FIXED: Changed from com.conference
import com.team5.demo.model.Session;  // ADDED: Need this for overlapping sessions check
import com.team5.demo.repository.RoomRepository;  // FIXED: Changed from com.conference
import com.team5.demo.repository.SessionRepository;  // FIXED: Changed from com.conference
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    private final RoomRepository roomRepository;
    private final SessionRepository sessionRepository;
    
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }
    
    public Room updateRoom(Long roomId, Room roomDetails) {
        Room room = getRoomById(roomId);
        room.setName(roomDetails.getName());
        room.setCapacity(roomDetails.getCapacity());
        return roomRepository.save(room);
    }
    
    public Room getRoomById(Long roomId) {
        return roomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));  // Simplified
    }
    
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }
    
    @Transactional
    public void deleteRoom(Long roomId) {
        Room room = getRoomById(roomId);
        roomRepository.delete(room);
    }
    
    public List<Room> getAvailableRooms(LocalDateTime startTime, LocalDateTime endTime) {
        return roomRepository.findAvailableRooms(startTime, endTime);
    }
    
    public boolean isRoomAvailable(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        List<Session> overlappingSessions = sessionRepository.findOverlappingSessions(
            roomId, startTime, endTime
        );
        return overlappingSessions.isEmpty();
    }
    
    public void validateRoomBooking(Long roomId, LocalDateTime startTime, LocalDateTime endTime) {
        if (!isRoomAvailable(roomId, startTime, endTime)) {
            throw new RuntimeException("Room is already booked for the specified time slot");  // Simplified
        }
    }
}
package com.team5.demo.repository;

import com.team5.demo.model.Room;  // FIXED: Changed from com.conference
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    
    @Query("SELECT r FROM Room r WHERE r.capacity >= :minCapacity")
    List<Room> findByMinCapacity(@Param("minCapacity") Integer minCapacity);
    
    @Query("SELECT r FROM Room r WHERE r.id NOT IN " +
           "(SELECT s.room.id FROM Session s WHERE " +
           "(:startTime BETWEEN s.startTime AND s.endTime) OR " +
           "(:endTime BETWEEN s.startTime AND s.endTime) OR " +
           "(s.startTime BETWEEN :startTime AND :endTime))")
    List<Room> findAvailableRooms(@Param("startTime") LocalDateTime startTime, 
                                   @Param("endTime") LocalDateTime endTime);
}
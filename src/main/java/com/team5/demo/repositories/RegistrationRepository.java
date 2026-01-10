package com.team5.demo.repositories;

import com.team5.demo.entities.Registration;
import com.team5.demo.entities.User;
import com.team5.demo.entities.Conference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {

    /* ===================== BASIC QUERIES ===================== */

    Optional<Registration> findByParticipantAndConference(
            User participant,
            Conference conference
    );

    List<Registration> findByParticipant(User participant);

    List<Registration> findByParticipantAndStatus(
            User participant,
            String status
    );

    List<Registration> findByConference(Conference conference);

    /* ===================== COUNTS ===================== */

    long countByConference(Conference conference);
    
    // boolean existsByParticipantIdAndConferenceId(Long userId, Long conferenceId);

    boolean existsByParticipantAndConference(User participant, Conference conference);
    long countByParticipant(User participant);

    /* ===================== EXISTS CHECK ===================== */

    boolean existsByParticipant_IdAndConference_Id(
            Long participantId,
            Long conferenceId
    );

    /* ===================== ADMIN QUERIES ===================== */

    @Query("""
        SELECT r FROM Registration r
        JOIN FETCH r.participant
        JOIN FETCH r.conference
        WHERE r.status = :status
    """)
    List<Registration> findByStatusWithRelations(@Param("status") String status);

    @Query("""
        SELECT r FROM Registration r
        JOIN FETCH r.participant
        JOIN FETCH r.conference
    """)
    List<Registration> findAllWithRelations();

    @Query("""
        SELECT r FROM Registration r
        JOIN FETCH r.participant p
        JOIN FETCH r.conference c
        WHERE r.status = 'CONFIRMED'
          AND (
               LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(p.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
          )
    """)
    List<Registration> searchConfirmedByParticipant(
            @Param("keyword") String keyword
    );

    // This query for User
    @Query("""
    SELECT r FROM Registration r
    JOIN FETCH r.conference
    WHERE r.participant.email = :email
    AND r.status = 'CONFIRMED'
        """)
        List<Registration> findMyRegistrationsByEmail(@Param("email") String email);



}

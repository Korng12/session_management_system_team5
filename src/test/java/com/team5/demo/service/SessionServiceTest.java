package com.team5.demo.service;

import com.team5.demo.model.Session;
import com.team5.demo.exception.ResourceNotFoundException;
import com.team5.demo.repositories.SessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session testSession;
    private final Long testSessionId = 1L;

    @BeforeEach
    void setUp() {
        testSession = new Session();
        testSession.setId(testSessionId);
        testSession.setTitle("Test Session");
        testSession.setDescription("Test Description");
        testSession.setStartTime(LocalDateTime.now().plusDays(1));
        testSession.setEndTime(LocalDateTime.now().plusDays(2));
    }

    @Test
    void getAllSessions_ShouldReturnListOfSessions() {
        // Arrange
        List<Session> sessions = Arrays.asList(testSession);
        when(sessionRepository.findAll()).thenReturn(sessions);

        // Act
        List<Session> result = sessionService.getAllSessions();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testSession.getTitle(), result.get(0).getTitle());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void getSessionById_ExistingId_ShouldReturnSession() {
        // Arrange
        when(sessionRepository.findById(testSessionId)).thenReturn(Optional.of(testSession));

        // Act
        Session result = sessionService.getSessionById(testSessionId);

        // Assert
        assertNotNull(result);
        assertEquals(testSessionId, result.getId());
        verify(sessionRepository, times(1)).findById(testSessionId);
    }

    @Test
    void getSessionById_NonExistingId_ShouldThrowException() {
        // Arrange
        when(sessionRepository.findById(testSessionId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sessionService.getSessionById(testSessionId);
        });
        verify(sessionRepository, times(1)).findById(testSessionId);
    }

    @Test
    void createSession_ValidSession_ShouldReturnSavedSession() {
        // Arrange
        when(sessionRepository.save(any(Session.class))).thenReturn(testSession);
        when(sessionRepository.existsByTitleAndStartTime(anyString(), any(LocalDateTime.class))).thenReturn(false);

        // Act
        Session result = sessionService.createSession(testSession);

        // Assert
        assertNotNull(result);
        assertEquals(testSession.getTitle(), result.getTitle());
        verify(sessionRepository, times(1)).save(any(Session.class));
        verify(sessionRepository, times(1)).existsByTitleAndStartTime(anyString(), any(LocalDateTime.class));
    }

    @Test
    void createSession_WithEndTimeBeforeStartTime_ShouldThrowException() {
        // Arrange
        Session invalidSession = new Session();
        invalidSession.setTitle("Invalid Session");
        invalidSession.setStartTime(LocalDateTime.now().plusDays(2));
        invalidSession.setEndTime(LocalDateTime.now().plusDays(1));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            sessionService.createSession(invalidSession);
        });
        verify(sessionRepository, never()).save(any(Session.class));
    }

    @Test
    void createSession_WithDuplicateTitleAndTime_ShouldThrowException() {
        // Arrange
        when(sessionRepository.existsByTitleAndStartTime(anyString(), any(LocalDateTime.class))).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            sessionService.createSession(testSession);
        });
        verify(sessionRepository, never()).save(any(Session.class));
        verify(sessionRepository, times(1)).existsByTitleAndStartTime(anyString(), any(LocalDateTime.class));
    }

    @Test
    void updateSession_ExistingSession_ShouldReturnUpdatedSession() {
        // Arrange
        Session updatedSession = new Session();
        updatedSession.setTitle("Updated Title");
        updatedSession.setDescription("Updated Description");
        updatedSession.setStartTime(LocalDateTime.now().plusDays(1));
        updatedSession.setEndTime(LocalDateTime.now().plusDays(2));

        when(sessionRepository.findById(testSessionId)).thenReturn(Optional.of(testSession));
        when(sessionRepository.save(any(Session.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Session result = sessionService.updateSession(testSessionId, updatedSession);

        // Assert
        assertNotNull(result);
        assertEquals(updatedSession.getTitle(), result.getTitle());
        assertEquals(updatedSession.getDescription(), result.getDescription());
        verify(sessionRepository, times(1)).findById(testSessionId);
        verify(sessionRepository, times(1)).save(any(Session.class));
    }

    @Test
    void deleteSession_ExistingSession_ShouldDeleteSession() {
        // Arrange
        when(sessionRepository.existsById(testSessionId)).thenReturn(true);
        doNothing().when(sessionRepository).deleteById(testSessionId);

        // Act
        sessionService.deleteSession(testSessionId);

        // Assert
        verify(sessionRepository, times(1)).existsById(testSessionId);
        verify(sessionRepository, times(1)).deleteById(testSessionId);
    }

    @Test
    void deleteSession_NonExistingSession_ShouldThrowException() {
        // Arrange
        when(sessionRepository.existsById(testSessionId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sessionService.deleteSession(testSessionId);
        });
        verify(sessionRepository, times(1)).existsById(testSessionId);
        verify(sessionRepository, never()).deleteById(anyLong());
    }
}

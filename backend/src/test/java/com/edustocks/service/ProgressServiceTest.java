package com.edustocks.service;

import com.edustocks.model.UserProgress;
import com.edustocks.repository.UserProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProgressServiceTest {

    @Mock
    private UserProgressRepository progressRepository;

    @InjectMocks
    private ProgressService progressService;

    private UserProgress testProgress;
    private String testUserId = "test-user-123";

    @BeforeEach
    void setUp() {
        testProgress = new UserProgress(testUserId);
    }

    @Test
    void testGetProgress_WhenExists() {
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(testProgress);

        UserProgress result = progressService.getUserProgress(testUserId);

        assertNotNull(result);
        assertEquals(testUserId, result.getUserId());
        assertEquals("beginner", result.getLevel());
        verify(progressRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void testGetProgress_WhenNotExists_CreatesNew() {
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(null);

        UserProgress result = progressService.getUserProgress(testUserId);

        assertNotNull(result);
        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }

    @Test
    void testCompleteLesson() {
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(testProgress);

        progressService.completeLesson(testUserId, "lesson-1", 85.0);

        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }

}



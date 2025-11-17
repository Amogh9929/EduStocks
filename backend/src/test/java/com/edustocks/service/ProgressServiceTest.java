package com.edustocks.service;

import com.edustocks.model.UserProgress;
import com.edustocks.repository.UserProgressRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.HashSet;

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
        testProgress = new UserProgress();
        testProgress.setUserId(testUserId);
        testProgress.setLevel(1);
        testProgress.setXp(0);
        testProgress.setCompletedLessons(new HashSet<>());
    }

    @Test
    void testGetProgress_WhenExists() {
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testProgress));

        UserProgress result = progressService.getProgress(testUserId);

        assertNotNull(result);
        assertEquals(testUserId, result.getUserId());
        assertEquals(1, result.getLevel());
        verify(progressRepository, times(1)).findByUserId(testUserId);
    }

    @Test
    void testGetProgress_WhenNotExists_CreatesNew() {
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(Optional.empty());
        when(progressRepository.save(any(UserProgress.class)))
            .thenReturn(testProgress);

        UserProgress result = progressService.getProgress(testUserId);

        assertNotNull(result);
        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }

    @Test
    void testAddXp_NoLevelUp() {
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testProgress));
        when(progressRepository.save(any(UserProgress.class)))
            .thenReturn(testProgress);

        progressService.addXp(testUserId, 50);

        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }

    @Test
    void testAddXp_WithLevelUp() {
        testProgress.setXp(950);
        
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testProgress));
        when(progressRepository.save(any(UserProgress.class)))
            .thenReturn(testProgress);

        progressService.addXp(testUserId, 100);

        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }

    @Test
    void testCompleteLesson() {
        when(progressRepository.findByUserId(testUserId))
            .thenReturn(Optional.of(testProgress));
        when(progressRepository.save(any(UserProgress.class)))
            .thenReturn(testProgress);

        progressService.completeLesson(testUserId, "lesson-1");

        verify(progressRepository, times(1)).save(any(UserProgress.class));
    }

    @Test
    void testCalculateRank_Novice() {
        testProgress.setLevel(5);
        String rank = progressService.calculateRank(testProgress);
        assertEquals("Novice", rank);
    }

    @Test
    void testCalculateRank_Expert() {
        testProgress.setLevel(75);
        String rank = progressService.calculateRank(testProgress);
        assertEquals("Expert", rank);
    }
}

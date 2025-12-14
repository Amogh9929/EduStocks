package com.edustocks.service;

import com.edustocks.model.UserProgress;
import com.edustocks.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {

    @Autowired
    private UserProgressRepository userProgressRepository;

    public UserProgress getUserProgress(String userId) {
        UserProgress progress = userProgressRepository.findByUserId(userId);
        if (progress == null) {
            progress = new UserProgress(userId);
            userProgressRepository.save(progress);
        }
        return progress;
    }

    public void completeLesson(String userId, String lessonId, double score) {
        UserProgress progress = getUserProgress(userId);
        
        if (!progress.getCompletedLessons().contains(lessonId)) {
            progress.getCompletedLessons().add(lessonId);
            
            // Award XP based on score
            int xpEarned = (int) (score / 10); // 10 XP per 1% score
            progress.setXp(progress.getXp() + xpEarned);
            
            // Update level based on XP
            updateLevel(progress);
            
            // Update rank
            updateRank(progress);
            
            userProgressRepository.save(progress);
        }
    }

    private void updateLevel(UserProgress progress) {
        int xp = progress.getXp();
        if (xp >= 5000) {
            progress.setLevel("advanced");
        } else if (xp >= 2000) {
            progress.setLevel("intermediate");
        } else {
            progress.setLevel("beginner");
        }
    }

    private void updateRank(UserProgress progress) {
        int xp = progress.getXp();
        if (xp >= 5000) {
            progress.setRank("Master");
        } else if (xp >= 2500) {
            progress.setRank("Expert");
        } else if (xp >= 1000) {
            progress.setRank("Advanced");
        } else if (xp >= 500) {
            progress.setRank("Intermediate");
        } else if (xp >= 100) {
            progress.setRank("Beginner");
        } else {
            progress.setRank("Novice");
        }
    }
}




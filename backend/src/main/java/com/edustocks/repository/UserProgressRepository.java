package com.edustocks.repository;

import com.edustocks.model.UserProgress;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserProgressRepository {
    
    // In-memory storage - replace with Firebase Firestore in production
    private final Map<String, UserProgress> progresses = new HashMap<>();

    public UserProgress findByUserId(String userId) {
        return progresses.get(userId);
    }

    public void save(UserProgress progress) {
        progresses.put(progress.getUserId(), progress);
    }

    public List<UserProgress> findAll() {
        return new ArrayList<>(progresses.values());
    }

    public void delete(String userId) {
        progresses.remove(userId);
    }
}




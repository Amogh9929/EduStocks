package com.edustocks.repository;

import com.edustocks.model.Lesson;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LessonRepository {
    
    // In-memory storage - replace with Firebase Firestore in production
    private final Map<String, Lesson> lessons = new HashMap<>();

    public Lesson findById(String id) {
        return lessons.get(id);
    }

    public void save(Lesson lesson) {
        lessons.put(lesson.getId(), lesson);
    }

    public List<Lesson> findAll() {
        return new ArrayList<>(lessons.values());
    }

    public void delete(String id) {
        lessons.remove(id);
    }
}




package com.edustocks.service;

import com.edustocks.model.Lesson;
import com.edustocks.model.Question;
import com.edustocks.repository.LessonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LessonService {

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ProgressService progressService;

    public List<Lesson> getAllLessons() {
        List<Lesson> lessons = lessonRepository.findAll();
        if (lessons.isEmpty()) {
            initializeDefaultLessons();
            lessons = lessonRepository.findAll();
        }
        return lessons;
    }

    public List<Lesson> getLessonsByLevel(String level) {
        List<Lesson> allLessons = getAllLessons();
        return allLessons.stream()
            .filter(lesson -> lesson.getLevel().equalsIgnoreCase(level))
            .collect(Collectors.toList());
    }

    public Lesson getLessonById(String id) {
        Lesson lesson = lessonRepository.findById(id);
        if (lesson == null) {
            // Initialize lessons if not found
            initializeDefaultLessons();
            lesson = lessonRepository.findById(id);
        }
        return lesson;
    }

    public void completeLesson(String userId, String lessonId, double score) {
        progressService.completeLesson(userId, lessonId, score);
    }

    // Initialize default lessons
    public void initializeDefaultLessons() {
        if (lessonRepository.findAll().isEmpty()) {
            List<Lesson> defaultLessons = createDefaultLessons();
            for (Lesson lesson : defaultLessons) {
                lessonRepository.save(lesson);
            }
        }
    }

    private List<Lesson> createDefaultLessons() {
        List<Lesson> lessons = new ArrayList<>();

        // Beginner Lesson 1
        Lesson lesson1 = new Lesson();
        lesson1.setId("lesson-1");
        lesson1.setTitle("What is a Stock?");
        lesson1.setDescription("Learn the basics of stocks and how they work");
        lesson1.setLevel("beginner");
        lesson1.setContent("<h2>What is a Stock?</h2><p>A stock represents ownership in a company...</p>");
        lesson1.setOrder(1);
        lesson1.setQuestions(createQuestionsForLesson1());
        lessons.add(lesson1);

        // Add more default lessons here...
        
        return lessons;
    }

    private List<Question> createQuestionsForLesson1() {
        List<Question> questions = new ArrayList<>();
        
        Question q1 = new Question();
        q1.setId("q1");
        q1.setQuestion("What does owning a stock mean?");
        q1.setOptions(List.of(
            "You own a piece of the company",
            "You lent money to the company",
            "You are an employee of the company",
            "You are a customer of the company"
        ));
        q1.setCorrectAnswer(0);
        q1.setExplanation("Owning a stock means you own a share of the company, making you a partial owner.");
        questions.add(q1);

        return questions;
    }
}


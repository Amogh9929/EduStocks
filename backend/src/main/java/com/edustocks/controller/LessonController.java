package com.edustocks.controller;

import com.edustocks.model.Lesson;
import com.edustocks.service.LessonService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<Lesson>> getLessons(@RequestParam(required = false) String level) {
        List<Lesson> lessons;
        if (level != null) {
            lessons = lessonService.getLessonsByLevel(level);
        } else {
            lessons = lessonService.getAllLessons();
        }
        return ResponseEntity.ok(lessons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLesson(@PathVariable String id) {
        Lesson lesson = lessonService.getLessonById(id);
        if (lesson != null) {
            return ResponseEntity.ok(lesson);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeLesson(
            @PathVariable String id,
            @RequestBody Map<String, Object> requestBody,
            HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        Double score = ((Number) requestBody.get("score")).doubleValue();

        lessonService.completeLesson(userId, id, score);
        
        Map<String, Object> response = new java.util.HashMap<>();
        response.put("success", true);
        response.put("message", "Lesson completed successfully");
        return ResponseEntity.ok(response);
    }
}




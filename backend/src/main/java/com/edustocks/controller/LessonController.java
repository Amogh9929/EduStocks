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
        
        // Validate inputs
        if (id == null || id.isBlank()) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Lesson ID is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String userId = (String) request.getAttribute("userId");
        if (userId == null || userId.isBlank()) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "User not authenticated");
            return ResponseEntity.status(401).body(errorResponse);
        }

        if (requestBody == null || !requestBody.containsKey("score")) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Score is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {
            Double score = ((Number) requestBody.get("score")).doubleValue();
            
            if (score < 0 || score > 100) {
                Map<String, Object> errorResponse = new java.util.HashMap<>();
                errorResponse.put("success", false);
                errorResponse.put("message", "Score must be between 0 and 100");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            lessonService.completeLesson(userId, id, score);
            
            Map<String, Object> response = new java.util.HashMap<>();
            response.put("success", true);
            response.put("message", "Lesson completed successfully");
            response.put("lessonId", id);
            response.put("score", score);
            response.put("completedAt", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (ClassCastException e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Invalid score format");
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new java.util.HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Error completing lesson: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}




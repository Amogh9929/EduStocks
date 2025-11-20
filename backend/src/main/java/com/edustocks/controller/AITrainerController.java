package com.edustocks.controller;

import com.edustocks.service.AITrainerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai-trainer")
public class AITrainerController {

    @Autowired
    private AITrainerService aiTrainerService;

    @PostMapping("/question")
    public ResponseEntity<Map<String, Object>> getQuestion(
            @RequestBody Map<String, String> requestBody) {
        String level = requestBody.get("level");
        String topic = requestBody.get("topic");

        Map<String, Object> question = aiTrainerService.generateQuestion(level, topic);
        return ResponseEntity.ok(question);
    }

    @PostMapping("/answer")
    public ResponseEntity<Map<String, Object>> submitAnswer(
            @RequestBody Map<String, Object> requestBody) {
        String questionId = (String) requestBody.get("questionId");
        Integer answer = (Integer) requestBody.get("answer");

        Map<String, Object> response = aiTrainerService.checkAnswer(questionId, answer);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> answerQuery(
            @RequestBody Map<String, String> requestBody,
            HttpServletRequest request) {
        // String userId = (String) request.getAttribute("userId");
        String query = requestBody.get("query");
        String level = requestBody.get("level");

        String response = aiTrainerService.answerQuery(query, level);
        
        Map<String, Object> result = new HashMap<>();
        result.put("response", response);
        return ResponseEntity.ok(result);
    }
}




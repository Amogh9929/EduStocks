package com.edustocks.controller;

import com.edustocks.model.UserProgress;
import com.edustocks.service.ProgressService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @Autowired
    private ProgressService progressService;

    @GetMapping
    public ResponseEntity<UserProgress> getProgress(HttpServletRequest request) {
        String userId = (String) request.getAttribute("userId");
        UserProgress progress = progressService.getUserProgress(userId);
        return ResponseEntity.ok(progress);
    }
}




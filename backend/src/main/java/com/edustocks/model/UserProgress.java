package com.edustocks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress {
    private String userId;
    private String level; // beginner, intermediate, advanced
    private List<String> completedLessons;
    private int xp;
    private String rank;

    public UserProgress(String userId) {
        this.userId = userId;
        this.level = "beginner";
        this.completedLessons = new ArrayList<>();
        this.xp = 0;
        this.rank = "Novice";
    }
}




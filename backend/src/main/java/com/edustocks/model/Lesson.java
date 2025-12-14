package com.edustocks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    private String id;
    private String title;
    private String description;
    private String level; // beginner, intermediate, advanced
    private String content;
    private List<Question> questions;
    private int order;
}




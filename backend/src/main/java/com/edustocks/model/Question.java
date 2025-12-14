package com.edustocks.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question {
    private String id;
    private String question;
    private List<String> options;
    private int correctAnswer;
    private String explanation;
}




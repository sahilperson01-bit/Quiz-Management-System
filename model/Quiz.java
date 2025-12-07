package com.quiz.model;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int quizId;
    private String title;
    private String description;
    private List<Question> questions;

    public Quiz(int quizId, String title, String description) {
        this.quizId = quizId;
        this.title = title;
        this.description = description;
        this.questions = new ArrayList<>();
    }

    public void addQuestion(Question q) {
        questions.add(q);
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public int getQuizId() { return quizId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}

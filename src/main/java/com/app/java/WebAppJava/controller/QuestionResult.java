package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Question;

public class QuestionResult {
    private Question question;
    private Long selectedAnswerId;
    private boolean correct;

    public QuestionResult(Question question, Long selectedAnswerId, boolean correct) {
        this.question = question;
        this.selectedAnswerId = selectedAnswerId;
        this.correct = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public Long getSelectedAnswerId() {
        return selectedAnswerId;
    }

    public boolean isCorrect() {
        return correct;
    }
}


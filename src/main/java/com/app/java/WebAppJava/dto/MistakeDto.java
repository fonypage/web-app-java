package com.app.java.WebAppJava.dto;

import java.time.LocalDateTime;

public class MistakeDto {
    private Long topicId;
    private String topicTitle;
    private String questionText;
    private String selectedAnswerText;
    private String correctAnswerText;
    private LocalDateTime timestamp;

    public MistakeDto(Long topicId, String topicTitle,
                      String questionText,
                      String selectedAnswerText,
                      String correctAnswerText,
                      LocalDateTime timestamp) {
        this.topicId = topicId;
        this.topicTitle = topicTitle;
        this.questionText = questionText;
        this.selectedAnswerText = selectedAnswerText;
        this.correctAnswerText = correctAnswerText;
        this.timestamp = timestamp;
    }

    public Long getTopicId() { return topicId; }
    public String getTopicTitle() { return topicTitle; }
    public String getQuestionText() { return questionText; }
    public String getSelectedAnswerText() { return selectedAnswerText; }
    public String getCorrectAnswerText() { return correctAnswerText; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
package com.app.java.WebAppJava.dto;

import java.time.LocalDateTime;

public class TopicProgressDto {
    private Long topicId;
    private String title;
    private int attempts;
    private int bestPercent;
    private int avgPercent;
    private LocalDateTime lastAttempt;
    private boolean mastered;

    public TopicProgressDto(Long topicId, String title, int attempts, int bestPercent, int avgPercent, LocalDateTime lastAttempt, boolean mastered) {
        this.topicId = topicId;
        this.title = title;
        this.attempts = attempts;
        this.bestPercent = bestPercent;
        this.avgPercent = avgPercent;
        this.lastAttempt = lastAttempt;
        this.mastered = mastered;
    }

    public Long getTopicId() { return topicId; }
    public String getTitle() { return title; }
    public int getAttempts() { return attempts; }
    public int getBestPercent() { return bestPercent; }
    public int getAvgPercent() { return avgPercent; }
    public LocalDateTime getLastAttempt() { return lastAttempt; }
    public boolean isMastered() { return mastered; }
}
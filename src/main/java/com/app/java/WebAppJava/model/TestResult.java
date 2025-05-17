package com.app.java.WebAppJava.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

import org.apache.catalina.User;
import org.hibernate.annotations.CreationTimestamp;
import javax.persistence.*;
import java.time.LocalDateTime;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "test_result")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1) имя пользователя
    @Column(nullable = false)
    private String username;

    // 2) id темы
    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    // 3) сколько правильно
    @Column(name = "correct_count", nullable = false)
    private int correctCount;

    // 4) сколько всего
    @Column(name = "total_count", nullable = false)
    private int totalCount;

    // 5) когда прошёл
    @Column(name = "taken_at", nullable = false, updatable = false)
    private LocalDateTime timestamp;

    // === конструкторы ===
    public TestResult() {}

    // === геттеры/сеттеры ===

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTopicId() {
        return topicId;
    }
    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public int getCorrectCount() {
        return correctCount;
    }
    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getTotalCount() {
        return totalCount;
    }
    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}


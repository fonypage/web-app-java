package com.app.java.WebAppJava.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "practice_task_solved",
        uniqueConstraints = @UniqueConstraint(name = "uk_solved_user_task", columnNames = {"username", "task_id"}))
public class PracticeTaskSolved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "solved_at", insertable = false, updatable = false)
    private LocalDateTime solvedAt;

    public PracticeTaskSolved() {}

    public PracticeTaskSolved(String username, Long taskId) {
        this.username = username;
        this.taskId = taskId;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public Long getTaskId() { return taskId; }
    public LocalDateTime getSolvedAt() { return solvedAt; }
}
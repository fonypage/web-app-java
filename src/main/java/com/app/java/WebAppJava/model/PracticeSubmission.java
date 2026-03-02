package com.app.java.WebAppJava.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "practice_submission")
public class PracticeSubmission {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(nullable = false)
    private String status; // OK/WA/CE/TLE/RE

    @Column(name = "passed_count", nullable = false)
    private int passedCount;

    @Column(name = "total_count", nullable = false)
    private int totalCount;

    @Column(name = "duration_ms")
    private Integer durationMs;

    @Lob
    @Column(name = "last_stdout")
    private String lastStdout;

    @Lob
    @Column(name = "last_stderr")
    private String lastStderr;

    @Lob
    @Column(nullable = false)
    private String code;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // getters/setters (можно Lombok)
    public void setUsername(String username) { this.username = username; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public void setStatus(String status) { this.status = status; }
    public void setPassedCount(int passedCount) { this.passedCount = passedCount; }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    public void setDurationMs(Integer durationMs) { this.durationMs = durationMs; }
    public void setLastStdout(String lastStdout) { this.lastStdout = lastStdout; }
    public void setLastStderr(String lastStderr) { this.lastStderr = lastStderr; }
    public void setCode(String code) { this.code = code; }
}
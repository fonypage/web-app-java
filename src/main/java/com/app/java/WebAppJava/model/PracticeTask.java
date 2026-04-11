package com.app.java.WebAppJava.model;

import javax.persistence.*;

@Entity
@Table(name = "practice_task")
public class PracticeTask {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "topic_id", nullable = false)
    private Long topicId;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(name = "description_html", nullable = false)
    private String descriptionHtml;

    @Lob
    @Column(name = "starter_code", nullable = false)
    private String starterCode;

    @Column(name = "time_limit_ms", nullable = false)
    private int timeLimitMs = 2000;

    @Column(name = "memory_mb", nullable = false)
    private int memoryMb = 256;

    @Column(name = "xp_reward", nullable = false)
    private int xpReward = 50;

    @Column(name = "checker_type", nullable = false)
    private String checkerType = "STDOUT";

    @Lob
    @Column(name = "junit_test_code")
    private String junitTestCode;

    public String getCheckerType() { return checkerType; }
    public String getJunitTestCode() { return junitTestCode; }

    public Long getId() { return id; }
    public Long getTopicId() { return topicId; }
    public String getTitle() { return title; }
    public String getDescriptionHtml() { return descriptionHtml; }
    public String getStarterCode() { return starterCode; }
    public int getTimeLimitMs() { return timeLimitMs; }
    public int getMemoryMb() { return memoryMb; }
    public int getXpReward() { return xpReward; }
}
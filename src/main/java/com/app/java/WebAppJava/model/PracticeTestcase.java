package com.app.java.WebAppJava.model;

import javax.persistence.*;

@Entity
@Table(name = "practice_testcase")
public class PracticeTestcase {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Lob
    @Column(name = "input_text", nullable = false)
    private String inputText;

    @Lob
    @Column(name = "expected_text", nullable = false)
    private String expectedText;

    @Column(name = "is_hidden", nullable = false)
    private boolean hidden;

    public Long getId() { return id; }
    public Long getTaskId() { return taskId; }
    public String getInputText() { return inputText; }
    public String getExpectedText() { return expectedText; }
    public boolean isHidden() { return hidden; }
}
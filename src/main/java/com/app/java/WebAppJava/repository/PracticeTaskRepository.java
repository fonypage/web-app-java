package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.PracticeTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PracticeTaskRepository extends JpaRepository<PracticeTask, Long> {
    Optional<PracticeTask> findFirstByTopicId(Long topicId);
}
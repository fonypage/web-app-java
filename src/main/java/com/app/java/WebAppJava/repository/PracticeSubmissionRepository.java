package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.PracticeSubmission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeSubmissionRepository extends JpaRepository<PracticeSubmission, Long> {
    List<PracticeSubmission> findTop10ByUsernameOrderByIdDesc(String username);
    long countByUsernameAndTaskId(String username, Long taskId);
}
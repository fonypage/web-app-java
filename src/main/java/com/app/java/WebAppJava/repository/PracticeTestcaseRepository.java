package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.PracticeTestcase;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PracticeTestcaseRepository extends JpaRepository<PracticeTestcase, Long> {
    List<PracticeTestcase> findByTaskIdOrderByIdAsc(Long taskId);
}
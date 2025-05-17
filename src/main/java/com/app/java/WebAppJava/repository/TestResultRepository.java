package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUsername(String username);
}


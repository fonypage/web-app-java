package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.TestResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultRepository extends JpaRepository<TestResult, Long> {
    List<TestResult> findByUsername(String username);
    List<TestResult> findByUsernameOrderByIdDesc(String username);

    // (2.2) XP по дням из тестов за последние N дней
    @Query(value = """
        SELECT DATE(tr.taken_at) AS day,
               SUM(tr.correct_count * 10 + CASE WHEN tr.correct_count = tr.total_count THEN 50 ELSE 0 END) AS xp
        FROM test_result tr
        WHERE tr.username = :username
          AND tr.taken_at >= DATE_SUB(CURDATE(), INTERVAL :days DAY)
        GROUP BY DATE(tr.taken_at)
        ORDER BY day
    """, nativeQuery = true)
    List<Object[]> sumTestXpByDay(@Param("username") String username, @Param("days") int days);

    // (2.3) Лучший % по тестам на тему
    @Query(value = """
        SELECT tr.topic_id,
               MAX(ROUND((tr.correct_count / tr.total_count) * 100)) AS best_percent
        FROM test_result tr
        WHERE tr.username = :username
        GROUP BY tr.topic_id
    """, nativeQuery = true)
    List<Object[]> bestPercentByTopic(@Param("username") String username);
}
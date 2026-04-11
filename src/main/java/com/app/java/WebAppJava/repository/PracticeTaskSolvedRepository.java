package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.PracticeTaskSolved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PracticeTaskSolvedRepository extends JpaRepository<PracticeTaskSolved, Long> {
    boolean existsByUsernameAndTaskId(String username, Long taskId);

    @Query("select s.taskId from PracticeTaskSolved s where s.username = :username")
    List<Long> findTaskIdsByUsername(@Param("username") String username);

    long countByUsername(String username);

    @Query(value = """
    SELECT DATE(s.solved_at) as day, SUM(t.xp_reward) as xp
    FROM practice_task_solved s
    JOIN practice_task t ON t.id = s.task_id
    WHERE s.username = :username
      AND s.solved_at >= DATE_SUB(CURDATE(), INTERVAL :days DAY)
    GROUP BY DATE(s.solved_at)
    ORDER BY day
""", nativeQuery = true)
    java.util.List<Object[]> sumPracticeXpByDay(@Param("username") String username, @Param("days") int days);

    @Query(value = """
    SELECT t.topic_id AS topic_id, COUNT(*) AS solved_count
    FROM practice_task_solved s
    JOIN practice_task t ON t.id = s.task_id
    WHERE s.username = :username
    GROUP BY t.topic_id
""", nativeQuery = true)
    List<Object[]> solvedPracticeByTopic(@Param("username") String username);

}
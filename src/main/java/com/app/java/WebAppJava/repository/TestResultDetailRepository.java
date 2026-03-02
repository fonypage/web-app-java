package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.TestResultDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TestResultDetailRepository extends JpaRepository<TestResultDetail, Long> {

    @Query("""
        select d from TestResultDetail d
        join fetch d.result r
        join fetch d.question q
        left join fetch d.selectedAnswer sa
        where r.username = :username
          and d.correct = false
        order by r.timestamp desc, d.id desc
    """)
    List<TestResultDetail> findLastMistakes(@Param("username") String username);
}
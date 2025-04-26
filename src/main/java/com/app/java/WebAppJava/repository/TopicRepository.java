package com.app.java.WebAppJava.repository;

import com.app.java.WebAppJava.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JpaRepository<Topic, Long> {
    List<Topic> findByType(String type);
    List<Topic> findByTypeAndTitleContainingIgnoreCase(String type, String title);
}

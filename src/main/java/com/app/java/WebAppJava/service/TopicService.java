package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> getAllTopics();
    List<Topic> getTopicsByType(String type);
    Topic getTopicById(Long id);
    List<Topic> getTopicsByTypeAndSearch(String type, String keyword);
}

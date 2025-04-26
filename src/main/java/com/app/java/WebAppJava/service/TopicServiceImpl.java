package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService{
    private final TopicRepository topicRepository;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    @Override
    public List<Topic> getTopicsByType(String type) {
        return topicRepository.findByType(type);
    }

    @Override
    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }
}

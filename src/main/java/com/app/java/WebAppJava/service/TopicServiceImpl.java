package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

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
    public Topic getTopicById(Long id) {
        return topicRepository.findById(id).orElse(null);
    }

    @Override
    public List<Topic> getTopicsByTypeAndSearch(String type, String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return topicRepository.findByType(type);
        } else {
            return topicRepository.findByTypeAndTitleContainingIgnoreCase(type, keyword);
        }
    }
    @Override
    public Topic saveTopic(Topic topic) {
        return topicRepository.save(topic);
    }

    @Override
    public void deleteTopicById(Long id) {
        topicRepository.deleteById(id);
    }
}


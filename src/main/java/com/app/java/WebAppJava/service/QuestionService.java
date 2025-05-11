package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.model.Question;

import java.util.List;

public interface QuestionService {
    List<Question> getQuestionsByTopicId(Long topicId);
}


package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService){
        this.topicService = topicService;
    }

    @GetMapping("/theory")
    public String showTheory(Model model){
        List<Topic> theoryTopics = topicService.getTopicsByType("theory");
        model.addAttribute("topics", theoryTopics);
        return "theory";
    }
    @GetMapping("/practice")
    public String showPractice(Model model) {
        List<Topic> practiceTopics = topicService.getTopicsByType("practice");
        model.addAttribute("topics", practiceTopics);
        return "practice";
    }

    @GetMapping("/topic/{id}")
    public String showTopicDetail(@PathVariable Long id, Model model) {
        Topic topic = topicService.getTopicById(id);
        model.addAttribute("topic", topic);
        return "topic-detail";
    }

    @GetMapping("/error")
    public String error(){
        return "error";
    }
}

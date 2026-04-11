package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.app.java.WebAppJava.repository.PracticeTaskRepository;

import java.util.List;

@Controller
public class TopicController {

    private final TopicService topicService;

    private final PracticeTaskRepository practiceTaskRepository;

    @Autowired
    public TopicController(TopicService topicService, PracticeTaskRepository practiceTaskRepository){
        this.topicService = topicService;
        this.practiceTaskRepository = practiceTaskRepository;
    }

    @GetMapping("/theory")
    public String showTheory(@RequestParam(required = false) String search, Model model) {
        List<Topic> topics = topicService.getTopicsByTypeAndSearch("theory", search);
        model.addAttribute("topics", topics);
        model.addAttribute("search", search);
        return "theory";
    }

    @GetMapping("/practice")
    public String showPractice(@RequestParam(required = false) String search, Model model) {
        List<Topic> topics = topicService.getTopicsByTypeAndSearch("practice", search);
        model.addAttribute("topics", topics);
        model.addAttribute("search", search);
        return "practice";
    }

    @GetMapping("/practice/topic/{topicId}")
    public String openPracticeByTopic(@PathVariable Long topicId) {
        var task = practiceTaskRepository.findFirstByTopicId(topicId)
                .orElseThrow(() -> new IllegalArgumentException("No practice task for topicId=" + topicId));
        return "redirect:/practice/task/" + task.getId();
    }

    @GetMapping("/topic/{id}")
    public String showTopicDetail(@PathVariable Long id, Model model) {
        Topic topic = topicService.getTopicById(id);
        topic.setViews(topic.getViews() + 1);
        topicService.saveTopic(topic);
        model.addAttribute("topic", topic);
        return "topic-detail";
    }
}

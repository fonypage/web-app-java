package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.service.TopicService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final TopicService topicService;

    public AdminController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Topic> topics = topicService.getAllTopics();

        int totalViews = topics.stream().mapToInt(Topic::getViews).sum();

        model.addAttribute("topics", topics);
        model.addAttribute("totalViews", totalViews == 0 ? 1 : totalViews);
        return "admin/dashboard";
    }


    @GetMapping("/topics/new")
    public String showAddForm(Model model) {
        model.addAttribute("topic", new Topic());
        return "admin/topic-form";
    }

    @GetMapping("/topics/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("topic", topicService.getTopicById(id));
        return "admin/topic-form";
    }

    @PostMapping("/topics/save")
    public String saveTopic(@ModelAttribute Topic topic) {
        topicService.saveTopic(topic);
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/topics/delete/{id}")
    public String deleteTopic(@PathVariable Long id) {
        topicService.deleteTopicById(id);
        return "redirect:/admin/dashboard";
    }
}

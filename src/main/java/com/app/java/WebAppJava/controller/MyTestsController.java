package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.TestResult;
import com.app.java.WebAppJava.repository.TestResultRepository;
import com.app.java.WebAppJava.repository.TopicRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MyTestsController {

    private final TestResultRepository testResultRepository;
    private final TopicRepository topicRepository;

    public MyTestsController(TestResultRepository testResultRepository, TopicRepository topicRepository) {
        this.testResultRepository = testResultRepository;
        this.topicRepository = topicRepository;
    }

    @GetMapping("/my-tests")
    public String showUserResults(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<TestResult> results = testResultRepository.findByUsername(username);
        model.addAttribute("results", results);
        model.addAttribute("topics", topicRepository.findAll());
        return "my-tests";
    }
}

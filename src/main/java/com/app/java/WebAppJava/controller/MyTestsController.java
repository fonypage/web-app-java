package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.TestResult;
import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.repository.TestResultRepository;
import com.app.java.WebAppJava.repository.TopicRepository;
import com.app.java.WebAppJava.service.UserProfileService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class MyTestsController {

    private final TestResultRepository testResultRepository;
    private final TopicRepository topicRepository;
    private final UserProfileService userProfileService;

    public MyTestsController(
            TestResultRepository testResultRepository,
            TopicRepository topicRepository,
            UserProfileService userProfileService
    ) {
        this.testResultRepository = testResultRepository;
        this.topicRepository = topicRepository;
        this.userProfileService = userProfileService;
    }

    @GetMapping("/my-tests")
    public String showUserResults(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<TestResult> results = testResultRepository.findByUsername(username);
        model.addAttribute("results", results);

        List<Topic> topics = topicRepository.findAll();
        Map<Long, String> topicTitleById = topics.stream()
                .collect(Collectors.toMap(Topic::getId, Topic::getTitle));

        model.addAttribute("topicTitleById", topicTitleById);

        model.addAttribute("profile", userProfileService.getSummary(username));

        return "my-tests";
    }

    @PostMapping("/my-tests/profile")
    public String updateProfile(
            @RequestParam(value = "institution", required = false) String institution,
            @RequestParam(value = "displayName", required = false) String displayName
    ) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userProfileService.updateProfile(username, institution, displayName);
        return "redirect:/my-tests";
    }
}
package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Answer;
import com.app.java.WebAppJava.model.Question;
import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.repository.AnswerRepository;
import com.app.java.WebAppJava.repository.QuestionRepository;
import com.app.java.WebAppJava.service.QuestionService;
import com.app.java.WebAppJava.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
public class TestController {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public TestController(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @GetMapping("/test/{topicId}")
    public String showTest(@PathVariable Long topicId, Model model) {
        List<Question> questions = questionRepository.findByTopicId(topicId);
        model.addAttribute("questions", questions);
        return "test";
    }
@PostMapping("/test-result")
public String handleTestResult(@RequestParam Long topicId, HttpServletRequest request, Model model) {
    List<Question> questions = questionRepository.findByTopicId(topicId);
    int correctCount = 0;
    int totalCount = questions.size();
    Map<Long, Long> selectedAnswers = new HashMap<>();
    List<QuestionResult> questionResults = new ArrayList<>();
    for (Question question : questions) {
        String paramName = "q_" + question.getId();
        String selectedAnswerIdStr = request.getParameter(paramName);
        if (selectedAnswerIdStr != null) {
            Long selectedAnswerId = Long.parseLong(selectedAnswerIdStr);
            selectedAnswers.put(question.getId(), selectedAnswerId);

            boolean isCorrect = question.getAnswers().stream()
                    .anyMatch(a -> a.getId().equals(selectedAnswerId) && a.isCorrect());
            if (isCorrect) correctCount++;
            questionResults.add(new QuestionResult(question, selectedAnswerId, isCorrect));
        } else {
            questionResults.add(new QuestionResult(question, null, false));
        }
    }
    model.addAttribute("correctCount", correctCount);
    model.addAttribute("totalCount", totalCount);
    model.addAttribute("questionResults", questionResults);
    return "test-result";
}
}


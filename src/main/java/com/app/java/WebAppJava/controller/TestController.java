package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Answer;
import com.app.java.WebAppJava.model.Question;
import com.app.java.WebAppJava.model.TestResult;
import com.app.java.WebAppJava.model.TestResultDetail;
import com.app.java.WebAppJava.repository.AnswerRepository;
import com.app.java.WebAppJava.repository.QuestionRepository;
import com.app.java.WebAppJava.repository.TestResultDetailRepository;
import com.app.java.WebAppJava.repository.TestResultRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.app.java.WebAppJava.service.UserXpService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class    TestController {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TestResultRepository testResultRepository;
    private final TestResultDetailRepository testResultDetailRepository;
    private final UserXpService userXpService;

    public TestController(QuestionRepository questionRepository,
                          AnswerRepository answerRepository,
                          TestResultRepository testResultRepository,
                          TestResultDetailRepository testResultDetailRepository,
                          UserXpService userXpService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.testResultRepository = testResultRepository;
        this.testResultDetailRepository = testResultDetailRepository;
        this.userXpService = userXpService;
    }

    @GetMapping("/test/{topicId}")
    public String showTest(@PathVariable Long topicId, Model model) {
        List<Question> questions = questionRepository.findByTopicId(topicId);
        model.addAttribute("questions", questions);
        model.addAttribute("topicId", topicId); // важно
        return "test";
    }

    @PostMapping("/test-result")
    public String handleTestResult(@RequestParam Long topicId, HttpServletRequest request, Model model) {
        List<Question> questions = questionRepository.findByTopicId(topicId);
        int correctCount = 0;
        int totalCount = questions.size();

        List<QuestionResult> questionResults = new ArrayList<>();

        for (Question question : questions) {
            String paramName = "q_" + question.getId();
            String selectedAnswerIdStr = request.getParameter(paramName);

            if (selectedAnswerIdStr != null) {
                Long selectedAnswerId = Long.parseLong(selectedAnswerIdStr);

                boolean isCorrect = question.getAnswers().stream()
                        .anyMatch(a -> a.getId().equals(selectedAnswerId) && a.isCorrect());

                if (isCorrect) correctCount++;

                questionResults.add(new QuestionResult(question, selectedAnswerId, isCorrect));
            } else {
                questionResults.add(new QuestionResult(question, null, false));
            }
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        TestResult result = new TestResult();
        result.setUsername(username);
        result.setTopicId(topicId);
        result.setCorrectCount(correctCount);
        result.setTotalCount(totalCount);
        result.setTimestamp(LocalDateTime.now());

        testResultRepository.save(result);

        List<TestResultDetail> details = new ArrayList<>();
        for (QuestionResult qr : questionResults) {
            TestResultDetail d = new TestResultDetail();
            d.setResult(result);
            d.setQuestion(qr.getQuestion());

            if (qr.getSelectedAnswerId() != null) {
                Answer selected = answerRepository.findById(qr.getSelectedAnswerId()).orElse(null);
                d.setSelectedAnswer(selected);
            } else {
                d.setSelectedAnswer(null);
            }

            d.setCorrect(qr.isCorrect());
            details.add(d);
        }
        testResultDetailRepository.saveAll(details);

        int xp = (correctCount * 10) + (correctCount == totalCount ? 50 : 0);
        userXpService.addXp(username, xp);
        model.addAttribute("xpAwarded", xp);

        model.addAttribute("correctCount", correctCount);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("questionResults", questionResults);
        return "test-result";
    }
}
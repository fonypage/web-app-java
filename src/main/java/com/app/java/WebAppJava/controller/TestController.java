package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.Answer;
import com.app.java.WebAppJava.model.Question;
import com.app.java.WebAppJava.model.TestResult;
import com.app.java.WebAppJava.model.Topic;
import com.app.java.WebAppJava.repository.AnswerRepository;
import com.app.java.WebAppJava.repository.QuestionRepository;
import com.app.java.WebAppJava.repository.TestResultRepository;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Controller
public class TestController {

    private final QuestionRepository questionRepository;
    private final TestResultRepository resultRepo;
    private final CookieUtil cookieUtil;

    @Autowired
    public TestController(QuestionRepository questionRepository,
                          TestResultRepository resultRepo,
                          CookieUtil cookieUtil) {
        this.questionRepository = questionRepository;
        this.resultRepo = resultRepo;
        this.cookieUtil = cookieUtil;
    }

    @GetMapping("/test/{topicId}")
    public String showTest(@PathVariable Long topicId, Model model) {
        model.addAttribute("questions", questionRepository.findByTopicId(topicId));
        model.addAttribute("topicId", topicId);
        return "test";
    }

    @PostMapping("/test-result")
    public String handleTestResult(
            @RequestParam Long topicId,
            HttpServletRequest req,
            HttpServletResponse resp,
            Model model) {

        // 1) Убедимся, что в ответе будет наша кука
        cookieUtil.ensureSessionCookie(req, resp);
        String sessionId = cookieUtil.getSessionId(req);

        // 2) Собираем и считаем
        List<Question> questions = questionRepository.findByTopicId(topicId);
        int correctCount = 0;
        for (Question q : questions) {
            String ans = req.getParameter("q_" + q.getId());
            if (ans!=null && q.getAnswers().stream()
                    .anyMatch(a -> a.getId().equals(Long.parseLong(ans)) && a.isCorrect())) {
                correctCount++;
            }
        }

        // 3) Сохраняем в БД
        TestResult r = new TestResult();
        r.setSessionId(sessionId);
        r.setCorrectCount(correctCount);
        resultRepo.save(r);

        // 4) Подготовим вывод
        model.addAttribute("correctCount", correctCount);
        model.addAttribute("totalCount", questions.size());
        return "test-result";
    }
}



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

//    @PostMapping("/test-result")
//    public String checkTest(@RequestParam Map<String, String> formData, Model model) {
//        int correct = 0;
//        int total = 0;
//
//        for (String key : formData.keySet()) {
//            if (key.startsWith("q_")) {
//                total++;
//                try {
//                    Long answerId = Long.parseLong(formData.get(key));
//                    Optional<Answer> answerOpt = answerRepository.findById(answerId);
//                    if (answerOpt.isPresent() && answerOpt.get().isCorrect()) {
//                        correct++;
//                    }
//                } catch (NumberFormatException e) {
//                    // некорректный id ответа — просто пропускаем
//                }
//            }
//        }
//
//        model.addAttribute("correctCount", correct);
//        model.addAttribute("totalCount", total);
//        return "test-result";
//    }
@PostMapping("/test-result")
public String handleTestResult(
        @RequestParam Long topicId,
        HttpServletRequest request,
        Model model
) {
    // Получаем все вопросы по теме
    List<Question> questions = questionRepository.findByTopicId(topicId);

    int correctCount = 0;
    int totalCount = questions.size();

    // Массив для хранения ID выбранных пользователем ответов
    Map<Long, Long> selectedAnswers = new HashMap<>();
    // Список вопросов с правильностью
    List<QuestionResult> questionResults = new ArrayList<>();

    for (Question question : questions) {
        String paramName = "q_" + question.getId();
        String selectedAnswerIdStr = request.getParameter(paramName);

        if (selectedAnswerIdStr != null) {
            Long selectedAnswerId = Long.parseLong(selectedAnswerIdStr);
            selectedAnswers.put(question.getId(), selectedAnswerId);

            // Проверка правильности
            boolean isCorrect = question.getAnswers().stream()
                    .anyMatch(a -> a.getId().equals(selectedAnswerId) && a.isCorrect());

            if (isCorrect) correctCount++;

            // Добавляем в список с результатом
            questionResults.add(new QuestionResult(question, selectedAnswerId, isCorrect));
        } else {
            // Пользователь не выбрал ответ
            questionResults.add(new QuestionResult(question, null, false));
        }
    }

    model.addAttribute("correctCount", correctCount);
    model.addAttribute("totalCount", totalCount);
    model.addAttribute("questionResults", questionResults);

    return "test-result";
}

}


package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.TestResult;
import com.app.java.WebAppJava.repository.TestResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

@Controller
public class MyTestsController {

    private final TestResultRepository resultRepo;
    private final CookieUtil cookieUtil;

    @Autowired
    public MyTestsController(TestResultRepository resultRepo, CookieUtil cookieUtil) {
        this.resultRepo = resultRepo;
        this.cookieUtil = cookieUtil;
    }

    @GetMapping("/my-tests")
    public String myTests(HttpServletRequest req, Model model) {
        String sessionId = cookieUtil.getSessionId(req);
        List<TestResult> list = sessionId == null
                ? Collections.emptyList()
                : resultRepo.findBySessionIdOrderByTakenAtDesc(sessionId);

        model.addAttribute("results", list);
        return "my-tests";
    }
}


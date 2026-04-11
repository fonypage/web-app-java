package com.app.java.WebAppJava.controller;

import com.app.java.WebAppJava.model.PracticeSubmission;
import com.app.java.WebAppJava.model.PracticeTask;
import com.app.java.WebAppJava.model.PracticeTaskSolved;
import com.app.java.WebAppJava.model.PracticeTestcase;
import com.app.java.WebAppJava.repository.PracticeSubmissionRepository;
import com.app.java.WebAppJava.repository.PracticeTaskRepository;
import com.app.java.WebAppJava.repository.PracticeTaskSolvedRepository;
import com.app.java.WebAppJava.repository.PracticeTestcaseRepository;
import com.app.java.WebAppJava.service.DockerJudgeService;
import com.app.java.WebAppJava.service.UserXpService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/practice/task")
public class PracticeTaskController {

    private final PracticeTaskRepository taskRepository;
    private final PracticeTestcaseRepository testcaseRepository;
    private final PracticeSubmissionRepository submissionRepository;
    private final PracticeTaskSolvedRepository solvedRepository;
    private final DockerJudgeService dockerJudgeService;

    private final UserXpService xpService;

    public PracticeTaskController(PracticeTaskRepository taskRepository,
                                  PracticeTestcaseRepository testcaseRepository,
                                  PracticeSubmissionRepository submissionRepository,
                                  PracticeTaskSolvedRepository solvedRepository,
                                  DockerJudgeService dockerJudgeService,
                                  UserXpService xpService) {
        this.taskRepository = taskRepository;
        this.testcaseRepository = testcaseRepository;
        this.submissionRepository = submissionRepository;
        this.solvedRepository = solvedRepository;
        this.dockerJudgeService = dockerJudgeService;
        this.xpService = xpService;
    }

    @GetMapping("/{taskId}")
    public String showTask(@PathVariable Long taskId,
                           @RequestParam(value = "submitted", required = false) String submitted,
                           Model model) {

        PracticeTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        List<PracticeTestcase> all = testcaseRepository.findByTaskIdOrderByIdAsc(taskId);
        List<PracticeTestcase> visibleCases = all.stream().filter(tc -> !tc.isHidden()).collect(Collectors.toList());

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        boolean solved = solvedRepository.existsByUsernameAndTaskId(username, taskId);
        model.addAttribute("solved", solved);

        PracticeSubmission last = submissionRepository.findTop10ByUsernameOrderByIdDesc(username).stream()
                .filter(s -> Objects.equals(s.getTaskId(), taskId))
                .findFirst()
                .orElse(null);

        String codeToShow = (last != null && last.getCode() != null && !last.getCode().isBlank())
                ? last.getCode()
                : task.getStarterCode();

        model.addAttribute("task", task);
        model.addAttribute("visibleCases", visibleCases);
        model.addAttribute("code", codeToShow);
        model.addAttribute("lastSubmission", last);
        model.addAttribute("submitted", submitted != null);

        return "practice-task";
    }



    @PostMapping("/{taskId}/submit")
    public String submit(@PathVariable Long taskId,
                         @RequestParam("code") String code) {

        PracticeTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        List<PracticeTestcase> testcases = testcaseRepository.findByTaskIdOrderByIdAsc(taskId);

        List<DockerJudgeService.TestCase> cases = testcases.stream()
                .map(tc -> new DockerJudgeService.TestCase(tc.getInputText(), tc.getExpectedText(), tc.isHidden()))
                .collect(Collectors.toList());

        DockerJudgeService.RunResult rr;

        if ("JUNIT".equalsIgnoreCase(task.getCheckerType())) {
            String testCode = task.getJunitTestCode();
            if (testCode == null || testCode.isBlank()) {
                rr = new DockerJudgeService.RunResult("RE", "", "JUnit test code is empty", 0, 0, 0);
            } else {
                rr = dockerJudgeService.judgeJUnit(code, testCode, task.getTimeLimitMs(), task.getMemoryMb());
            }
        } else {
            rr = dockerJudgeService.judge(code, cases, task.getTimeLimitMs(), task.getMemoryMb());
        }

        PracticeSubmission sub = new PracticeSubmission();
        sub.setUsername(username);
        sub.setTaskId(taskId);
        sub.setStatus(rr.status);
        sub.setPassedCount(rr.passed);
        sub.setTotalCount(rr.total);
        sub.setDurationMs(rr.durationMs);

        sub.setLastStdout(toAsciiSafe(rr.stdout));
        sub.setLastStderr(toAsciiSafe(rr.stderr));

        sub.setCode(code);
        submissionRepository.save(sub);

        if ("OK".equals(rr.status)) {
            boolean alreadySolved = solvedRepository.existsByUsernameAndTaskId(username, taskId);
            if (!alreadySolved) {
                solvedRepository.save(new PracticeTaskSolved(username, taskId));

                try {
                    if (xpService != null) {
                        xpService.addXp(username, task.getXpReward());
                    }
                } catch (Exception ignored) {
                }
            }
        }

        return "redirect:/practice/task/" + taskId + "?submitted=1";
    }

    private static String toAsciiSafe(String s) {
        if (s == null) return null;
        StringBuilder sb = new StringBuilder(Math.min(s.length(), 3000));
        int max = Math.min(s.length(), 3000);
        for (int i = 0; i < max; i++) {
            char c = s.charAt(i);
            if (c == '\n' || c == '\r' || c == '\t') sb.append(c);
            else if (c >= 32 && c <= 126) sb.append(c);
            else sb.append('?');
        }
        return sb.toString();
    }
}
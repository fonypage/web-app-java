package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.dto.AchievementDto;
import com.app.java.WebAppJava.dto.MistakeDto;
import com.app.java.WebAppJava.dto.UserProfileSummaryDto;
import com.app.java.WebAppJava.model.*;
import com.app.java.WebAppJava.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.app.java.WebAppJava.dto.TopicProgressDto;


import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final TestResultRepository testResultRepository;
    private final TopicRepository topicRepository;
    private final TestResultDetailRepository testResultDetailRepository;
    private final PracticeTaskSolvedRepository practiceTaskSolvedRepository;
    private final PracticeSubmissionRepository practiceSubmissionRepository;


    public UserProfileServiceImpl(UserProfileRepository userProfileRepository,
                                  TestResultRepository testResultRepository,
                                  TopicRepository topicRepository,
                                  TestResultDetailRepository testResultDetailRepository,
                                  PracticeTaskSolvedRepository practiceTaskSolvedRepository,
                                  PracticeSubmissionRepository practiceSubmissionRepository) {
        this.userProfileRepository = userProfileRepository;
        this.testResultRepository = testResultRepository;
        this.topicRepository = topicRepository;
        this.testResultDetailRepository = testResultDetailRepository;
        this.practiceTaskSolvedRepository = practiceTaskSolvedRepository;
        this.practiceSubmissionRepository = practiceSubmissionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserProfileSummaryDto getSummary(String username) {
        UserProfile profile = userProfileRepository.findById(username)
                .orElseGet(() -> userProfileRepository.save(new UserProfile(username)));

        List<TestResult> results = testResultRepository.findByUsernameOrderByIdDesc(username);

        Map<Long, String> topicTitleById = topicRepository.findAll().stream()
                .collect(Collectors.toMap(Topic::getId, Topic::getTitle));

        List<TopicProgressDto> topicProgress = buildTopicProgress(results, topicTitleById);

        int masteredTopicsCount = (int) topicProgress.stream().filter(TopicProgressDto::isMastered).count();
        int totalTopics = topicTitleById.size();

        List<MistakeDto> lastMistakes = buildLastMistakes(username, topicTitleById, 5);

        int totalTests = results.size();

        List<Integer> percents = results.stream()
                .map(this::percent)
                .collect(Collectors.toList());


        int bestPercent = percents.stream().max(Integer::compareTo).orElse(0);
        int avgPercent = percents.isEmpty() ? 0 : (int) Math.round(percents.stream().mapToInt(x -> x).average().orElse(0));

        int xp = profile.getXp();

        int level = 1 + (xp / 500);
        int xpToNextLevel = 500 - (xp % 500);
        if (xpToNextLevel == 500) xpToNextLevel = 0;

        int streakDays = computeStreakDays(results);

        long solvedPractice = practiceTaskSolvedRepository.countByUsername(username);

        boolean practiceFirst = solvedPractice >= 1;
        boolean practice3 = solvedPractice >= 3;
        boolean practice5 = solvedPractice >= 5;
        boolean practice10 = solvedPractice >= 10;

        boolean practiceFirstTry = practiceTaskSolvedRepository.findTaskIdsByUsername(username).stream()
                .anyMatch(taskId -> practiceSubmissionRepository.countByUsernameAndTaskId(username, taskId) == 1);

        List<AchievementDto> achievements =
                buildAchievements(results, totalTests, bestPercent, streakDays,
                        practiceFirst, practice3, practice5, practice10, practiceFirstTry);

        String shownName = (profile.getDisplayName() != null && !profile.getDisplayName().isBlank())
                ? profile.getDisplayName()
                : username;

        return new UserProfileSummaryDto(
                username,
                shownName,
                profile.getInstitution(),
                totalTests,
                bestPercent,
                avgPercent,
                xp,
                level,
                xpToNextLevel,
                streakDays,
                achievements,
                topicProgress,
                masteredTopicsCount,
                totalTopics,
                lastMistakes
        );
    }

    @Override
    @Transactional
    public void updateInstitution(String username, String institution) {
        updateProfile(username, institution, null);
    }

    @Override
    @Transactional
    public void updateProfile(String username, String institution, String displayName) {
        UserProfile profile = userProfileRepository.findById(username)
                .orElseGet(() -> new UserProfile(username));

        if (institution != null) {
            profile.setInstitution(normalizeInstitution(institution));
        }

        if (displayName != null) {
            String dn = displayName.trim();
            if (dn.isEmpty()) dn = null;

            if (dn != null) {
                if (dn.length() > 32) dn = dn.substring(0, 32);
                dn = dn.replaceAll("[<>\"'\\\\]", "");
            }

            profile.setDisplayName(dn);
        }

        userProfileRepository.save(profile);
    }

    private String normalizeInstitution(String s) {
        if (s == null) return null;
        String v = s.trim();
        return v.isEmpty() ? null : v;
    }

    private int percent(TestResult r) {
        int total = safeTotal(r);
        if (total <= 0) return 0;
        return (int) Math.round((safeCorrect(r) * 100.0) / total);
    }

    private int safeCorrect(TestResult r) {
        return r.getCorrectCount();
    }

    private int safeTotal(TestResult r) {
        return r.getTotalCount();
    }

    private int computeStreakDays(List<TestResult> results) {
        Set<LocalDate> days = results.stream()
                .map(this::toLocalDateSafe)  // если вернёт null — фильтруем
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new)); // отсортировано по возрастанию

        if (days.isEmpty()) return 0;

        LocalDate current = ((TreeSet<LocalDate>) days).last();
        int streak = 0;

        while (days.contains(current)) {
            streak++;
            current = current.minusDays(1);
        }
        return streak;
    }

    private LocalDate toLocalDateSafe(TestResult r) {
        return r.getTimestamp() == null ? null : r.getTimestamp().toLocalDate();
    }

    private List<AchievementDto> buildAchievements(
            List<TestResult> results,
            int totalTests,
            int bestPercent,
            int streakDays,
            boolean practiceFirst,
            boolean practice3,
            boolean practice5,
            boolean practice10,
            boolean practiceFirstTry
    ) {
        Set<Long> masteredTopics = results.stream()
                .filter(r -> percent(r) >= 80)
                .map(this::topicIdSafe)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        boolean firstTest = totalTests >= 1;
        boolean tenTests = totalTests >= 10;
        boolean perfect = bestPercent == 100;
        boolean streak3 = streakDays >= 3;
        boolean fiveTopics = masteredTopics.size() >= 5;

        return List.of(
                new AchievementDto("FIRST_TEST", "🏁 Первый тест", "Пройди 1 тест", firstTest),
                new AchievementDto("PERFECT", "🎯 Идеальный результат", "Получи 100% хотя бы раз", perfect),
                new AchievementDto("TEN_TESTS", "🧠 10 тестов", "Пройди 10 тестов", tenTests),
                new AchievementDto("STREAK_3", "🔥 Серия 3 дня", "Проходи тесты 3 дня подряд", streak3),
                new AchievementDto("MASTER_5_TOPICS", "⭐ 5 тем освоено", "Набери ≥80% в 5 разных темах", fiveTopics),

                new AchievementDto("PRACTICE_FIRST", "🧩 Первая задача", "Реши 1 практическую задачу", practiceFirst),
                new AchievementDto("PRACTICE_FIRST_TRY", "⚡ С первого раза", "Реши задачу с первой попытки", practiceFirstTry),
                new AchievementDto("PRACTICE_3", "🧩 3 задачи", "Реши 3 практические задачи", practice3),
                new AchievementDto("PRACTICE_5", "🧩 5 задач", "Реши 5 практических задач", practice5),
                new AchievementDto("PRACTICE_10", "🧩 10 задач", "Реши 10 практических задач", practice10)
        );
    }

    private List<TopicProgressDto> buildTopicProgress(List<TestResult> results, Map<Long, String> topicTitleById) {
        if (results == null || results.isEmpty()) return List.of();

        Map<Long, List<TestResult>> byTopic = results.stream()
                .filter(r -> topicIdSafe(r) != null)
                .collect(Collectors.groupingBy(this::topicIdSafe));

        List<TopicProgressDto> list = new ArrayList<>();

        for (Map.Entry<Long, List<TestResult>> e : byTopic.entrySet()) {
            Long topicId = e.getKey();
            List<TestResult> topicResults = e.getValue();

            int attempts = topicResults.size();

            List<Integer> percents = topicResults.stream().map(this::percent).collect(Collectors.toList());
            int best = percents.stream().max(Integer::compareTo).orElse(0);
            int avg = percents.isEmpty() ? 0 : (int) Math.round(percents.stream().mapToInt(x -> x).average().orElse(0));

            var lastAttempt = topicResults.stream()
                    .map(TestResult::getTimestamp)
                    .filter(Objects::nonNull)
                    .max(Comparator.naturalOrder())
                    .orElse(null);

            boolean mastered = best >= 80;

            String title = topicTitleById.getOrDefault(topicId, "Тема #" + topicId);

            list.add(new TopicProgressDto(topicId, title, attempts, best, avg, lastAttempt, mastered));
        }

        list.sort(Comparator
                .comparing(TopicProgressDto::isMastered).reversed()
                .thenComparing(TopicProgressDto::getBestPercent, Comparator.reverseOrder())
                .thenComparing(tp -> tp.getLastAttempt() == null ? java.time.LocalDateTime.MIN : tp.getLastAttempt(), Comparator.reverseOrder())
        );

        return list;
    }

    private List<MistakeDto> buildLastMistakes(String username, Map<Long, String> topicTitleById, int limit) {
        List<TestResultDetail> details = testResultDetailRepository.findLastMistakes(username);
        if (details == null || details.isEmpty()) return List.of();

        List<MistakeDto> list = new ArrayList<>();

        for (TestResultDetail d : details) {
            if (list.size() >= limit) break;

            TestResult tr = d.getResult();
            Long topicId = tr.getTopicId();
            String topicTitle = topicTitleById.getOrDefault(topicId, "Тема #" + topicId);

            String questionText = d.getQuestion() != null ? d.getQuestion().getQuestion() : "Вопрос";
            String selected = d.getSelectedAnswer() != null ? d.getSelectedAnswer().getText() : "—";

            String correctAnswer = "—";
            if (d.getQuestion() != null && d.getQuestion().getAnswers() != null) {
                for (Answer a : d.getQuestion().getAnswers()) {
                    if (a.isCorrect()) { correctAnswer = a.getText(); break; }
                }
            }

            list.add(new MistakeDto(topicId, topicTitle, questionText, selected, correctAnswer, tr.getTimestamp()));
        }

        return list;
    }

    private Long topicIdSafe(TestResult r) {
        return r.getTopicId();
    }
}
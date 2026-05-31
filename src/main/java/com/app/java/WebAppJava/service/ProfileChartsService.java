package com.app.java.WebAppJava.service;

import com.app.java.WebAppJava.dto.ProfileChartsDto;
import com.app.java.WebAppJava.repository.PracticeTaskSolvedRepository;
import com.app.java.WebAppJava.repository.TestResultRepository;
import com.app.java.WebAppJava.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ProfileChartsService {

    private final PracticeTaskSolvedRepository solvedRepo;
    private final TestResultRepository testRepo;
    private final TopicRepository topicRepo;

    public ProfileChartsService(PracticeTaskSolvedRepository solvedRepo,
                                TestResultRepository testRepo,
                                TopicRepository topicRepo) {
        this.solvedRepo = solvedRepo;
        this.testRepo = testRepo;
        this.topicRepo = topicRepo;
    }

    public ProfileChartsDto build(String username, int daysBack) {

        Map<LocalDate, Integer> xpMap = new HashMap<>();

        for (Object[] r : solvedRepo.sumPracticeXpByDay(username, daysBack)) {
            LocalDate d = LocalDate.parse(r[0].toString());
            int xp = ((Number) r[1]).intValue();
            xpMap.merge(d, xp, Integer::sum);
        }

        for (Object[] r : testRepo.sumTestXpByDay(username, daysBack)) {
            LocalDate d = LocalDate.parse(r[0].toString());
            int xp = ((Number) r[1]).intValue();
            xpMap.merge(d, xp, Integer::sum);
        }

        List<String> days = new ArrayList<>();
        List<Integer> xpPerDay = new ArrayList<>();
        List<Integer> xpCumulative = new ArrayList<>();

        LocalDate start = LocalDate.now().minusDays(daysBack - 1L);
        int cum = 0;

        for (int i = 0; i < daysBack; i++) {
            LocalDate d = start.plusDays(i);
            int xp = xpMap.getOrDefault(d, 0);
            days.add(d.toString());
            xpPerDay.add(xp);
            cum += xp;
            xpCumulative.add(cum);
        }

        Map<Long, Integer> bestPercentByTopic = new HashMap<>();
        for (Object[] r : testRepo.bestPercentByTopic(username)) {
            long topicId = ((Number) r[0]).longValue();
            int best = ((Number) r[1]).intValue();
            bestPercentByTopic.put(topicId, best);
        }

        Map<Long, Integer> solvedPracticeByTopic = new HashMap<>();
        for (Object[] r : solvedRepo.solvedPracticeByTopic(username)) {
            long topicId = ((Number) r[0]).longValue();
            int count = ((Number) r[1]).intValue();
            solvedPracticeByTopic.put(topicId, count);
        }

        Set<Long> topicIds = new HashSet<>();
        topicIds.addAll(bestPercentByTopic.keySet());
        topicIds.addAll(solvedPracticeByTopic.keySet());

        Map<Long, String> titles = new HashMap<>();
        topicRepo.findAllById(topicIds).forEach(t -> titles.put(t.getId(), t.getTitle()));

        List<Long> orderedIds = new ArrayList<>(topicIds);
        orderedIds.sort(Comparator.comparing(id -> titles.getOrDefault(id, "topic#" + id)));

        List<String> topicLabels = new ArrayList<>();
        List<Integer> bestPercent = new ArrayList<>();
        List<Integer> practiceSolved = new ArrayList<>();

        for (Long id : orderedIds) {
            topicLabels.add(titles.getOrDefault(id, "Тема #" + id));
            bestPercent.add(bestPercentByTopic.getOrDefault(id, 0));
            practiceSolved.add(solvedPracticeByTopic.getOrDefault(id, 0));
        }

        return new ProfileChartsDto(days, xpPerDay, xpCumulative, topicLabels, bestPercent, practiceSolved);
    }
}
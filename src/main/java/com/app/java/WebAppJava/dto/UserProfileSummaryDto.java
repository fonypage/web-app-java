package com.app.java.WebAppJava.dto;

import java.util.List;

public class UserProfileSummaryDto {
    private final String username;
    private final String displayName;
    private final String institution;

    private final int totalTests;
    private final int bestPercent;
    private final int avgPercent;

    private final int xp;
    private final int level;
    private final int xpToNextLevel;
    private final int streakDays;

    private final List<AchievementDto> achievements;

    private List<TopicProgressDto> topicProgress;
    private int masteredTopics;
    private int totalTopics;

    private List<MistakeDto> lastMistakes;

    public List<MistakeDto> getLastMistakes() { return lastMistakes; }

    public UserProfileSummaryDto(
            String username,
            String institution,
            int totalTests,
            int bestPercent,
            int avgPercent,
            int xp,
            int level,
            int xpToNextLevel,
            int streakDays,
            List<AchievementDto> achievements,
            List<TopicProgressDto> topicProgress,
            int masteredTopics,
            int totalTopics,
            List<MistakeDto> lastMistakes
    ) {
        this.username = username;
        this.displayName = username;
        this.institution = institution;
        this.totalTests = totalTests;
        this.bestPercent = bestPercent;
        this.avgPercent = avgPercent;
        this.xp = xp;
        this.level = level;
        this.xpToNextLevel = xpToNextLevel;
        this.streakDays = streakDays;
        this.achievements = achievements;
        this.topicProgress = topicProgress;
        this.masteredTopics = masteredTopics;
        this.totalTopics = totalTopics;
        this.lastMistakes = lastMistakes;
    }

    public UserProfileSummaryDto(
            String username,
            String displayName,
            String institution,
            int totalTests,
            int bestPercent,
            int avgPercent,
            int xp,
            int level,
            int xpToNextLevel,
            int streakDays,
            List<AchievementDto> achievements,
            List<TopicProgressDto> topicProgress,
            int masteredTopics,
            int totalTopics,
            List<MistakeDto> lastMistakes
    ) {
        this.username = username;
        this.displayName = displayName;
        this.institution = institution;
        this.totalTests = totalTests;
        this.bestPercent = bestPercent;
        this.avgPercent = avgPercent;
        this.xp = xp;
        this.level = level;
        this.xpToNextLevel = xpToNextLevel;
        this.streakDays = streakDays;
        this.achievements = achievements;
        this.topicProgress = topicProgress;
        this.masteredTopics = masteredTopics;
        this.totalTopics = totalTopics;
        this.lastMistakes = lastMistakes;
    }

    public String getUsername() { return username; }
    public String getDisplayName() { return displayName; }
    public String getInstitution() { return institution; }
    public int getTotalTests() { return totalTests; }
    public int getBestPercent() { return bestPercent; }
    public int getAvgPercent() { return avgPercent; }
    public int getXp() { return xp; }
    public int getLevel() { return level; }
    public int getXpToNextLevel() { return xpToNextLevel; }
    public int getStreakDays() { return streakDays; }
    public List<AchievementDto> getAchievements() { return achievements; }
    public List<TopicProgressDto> getTopicProgress() { return topicProgress; }
    public int getMasteredTopics() { return masteredTopics; }
    public int getTotalTopics() { return totalTopics; }
}
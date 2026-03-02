package com.app.java.WebAppJava.dto;

public class AchievementDto {
    private final String code;
    private final String title;
    private final String description;
    private final boolean unlocked;

    public AchievementDto(String code, String title, String description, boolean unlocked) {
        this.code = code;
        this.title = title;
        this.description = description;
        this.unlocked = unlocked;
    }

    public String getCode() { return code; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isUnlocked() { return unlocked; }
}
package com.app.java.WebAppJava.dto;

import java.util.List;

public class ProfileChartsDto {
    public final List<String> days;          // yyyy-MM-dd
    public final List<Integer> xpPerDay;
    public final List<Integer> xpCumulative;

    public final List<String> topicLabels;
    public final List<Integer> bestPercent;
    public final List<Integer> practiceSolved;

    public ProfileChartsDto(List<String> days,
                            List<Integer> xpPerDay,
                            List<Integer> xpCumulative,
                            List<String> topicLabels,
                            List<Integer> bestPercent,
                            List<Integer> practiceSolved) {
        this.days = days;
        this.xpPerDay = xpPerDay;
        this.xpCumulative = xpCumulative;
        this.topicLabels = topicLabels;
        this.bestPercent = bestPercent;
        this.practiceSolved = practiceSolved;
    }
}
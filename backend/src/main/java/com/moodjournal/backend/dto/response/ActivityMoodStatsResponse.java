// ActivityMoodStatsResponse.java
package com.moodjournal.backend.dto.response;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class ActivityMoodStatsResponse {
    private Long activityId;
    private String activityName;
    private String icon;
    private String color;
    private Double averageMoodScore;
    private Long entryCount;
}
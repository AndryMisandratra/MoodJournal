package com.moodjournal.backend.dto.response;

import com.moodjournal.backend.entity.MoodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class EntryResponse {
    private Long id;
    private MoodType mood;
    private String note;
    private LocalDateTime recordedAt;
    private List<ActivitySummary> activities;

    @Data
    @AllArgsConstructor
    public static class ActivitySummary {
        private Long id;
        private String name;
        private String icon;
        private String color;
    }
}
package com.moodjournal.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data @AllArgsConstructor
public class WeeklyTrendResponse {
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private Double averageMoodScore;
}
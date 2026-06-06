package com.moodjournal.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class BestDayResponse {
    private String dayOfWeek;
    private Double averageMoodScore;
}
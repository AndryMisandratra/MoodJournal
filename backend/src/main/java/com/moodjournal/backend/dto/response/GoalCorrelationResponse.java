package com.moodjournal.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GoalCorrelationResponse {
    private Long goalId;
    private String goalTitle;
    private Double moodWhenCompleted;
    private Double moodWhenNotCompleted;
    private Double improvement;
}
package com.moodjournal.backend.dto.response;

import com.moodjournal.backend.entity.GoalStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data @AllArgsConstructor
public class GoalResponse {
    private Long id;
    private String title;
    private Long targetActivityId;
    private String targetActivityName;
    private Integer currentStreak;
    private Integer longestStreak;
    private GoalStatus status;
    private LocalDateTime lastCompletedAt;
}
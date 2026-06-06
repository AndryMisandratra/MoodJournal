package com.moodjournal.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateGoalRequest {
    @NotBlank
    private String title;

    @NotNull
    private Long targetActivityId;
}
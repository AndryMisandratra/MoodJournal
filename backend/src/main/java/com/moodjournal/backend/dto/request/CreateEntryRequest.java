package com.moodjournal.backend.dto.request;

import com.moodjournal.backend.entity.MoodType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class CreateEntryRequest {
    @NotNull
    private MoodType mood;
    private String note;
    private List<Long> activityIds;
}
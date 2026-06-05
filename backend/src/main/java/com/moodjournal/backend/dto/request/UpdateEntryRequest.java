package com.moodjournal.backend.dto.request;

import com.moodjournal.backend.entity.MoodType;
import lombok.Data;
import java.util.List;

@Data
public class UpdateEntryRequest {
    private MoodType mood;
    private String note;
    private List<Long> activityIds;
}
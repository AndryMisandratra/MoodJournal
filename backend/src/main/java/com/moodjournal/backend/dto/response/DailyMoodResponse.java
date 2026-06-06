package com.moodjournal.backend.dto.response;

import com.moodjournal.backend.entity.MoodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class DailyMoodResponse {
    private LocalDate date;
    private MoodType mood; // peut être null
}
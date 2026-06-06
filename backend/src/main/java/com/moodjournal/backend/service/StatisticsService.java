package com.moodjournal.backend.service;

import com.moodjournal.backend.dto.response.*;
import com.moodjournal.backend.entity.Goal;
import com.moodjournal.backend.repository.GoalRepository;
import com.moodjournal.backend.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsService {

    private final JournalEntryRepository journalEntryRepository;
    private final GoalRepository goalRepository;

    public List<ActivityMoodStatsResponse> getActivityMoodStats(Long userId, int daysBack) {
        LocalDateTime since = LocalDateTime.now().minusDays(daysBack);
        List<Object[]> rows = journalEntryRepository.findAverageMoodByActivitySince(userId, since);
        return rows.stream()
                .map(row -> new ActivityMoodStatsResponse(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (Double) row[4],
                        (Long) row[5]
                )).collect(Collectors.toList());
    }

    public List<WeeklyTrendResponse> getWeeklyTrend(Long userId, int weeksCount) {
        Pageable limit = PageRequest.of(0, weeksCount);
        List<Object[]> rows = journalEntryRepository.findWeeklyAverageMood(userId, limit);
        return rows.stream()
                .map(row -> {
                    // row[0] est un Timestamp (ou LocalDateTime selon le driver)
                    LocalDate weekStart;
                    if (row[0] instanceof Timestamp) {
                        weekStart = ((Timestamp) row[0]).toLocalDateTime().toLocalDate();
                    } else if (row[0] instanceof LocalDateTime) {
                        weekStart = ((LocalDateTime) row[0]).toLocalDate();
                    } else {
                        throw new RuntimeException("Unexpected type for weekStart: " + row[0].getClass());
                    }
                    LocalDate weekEnd = weekStart.plusDays(6);
                    Double avg = (Double) row[1];
                    return new WeeklyTrendResponse(weekStart, weekEnd, avg);
                }).collect(Collectors.toList());
    }

    public BestDayResponse getBestDayOfWeek(Long userId) {
        List<Object[]> rows = journalEntryRepository.findBestDayOfWeek(userId);
        if (rows.isEmpty()) {
            return new BestDayResponse("UNKNOWN", 0.0);
        }
        Object[] best = rows.get(0);
        int dow = ((Number) best[0]).intValue(); // 1=Monday, 2=Tuesday, ..., 7=Sunday
        String dayName = switch (dow) {
            case 1 -> "MONDAY";
            case 2 -> "TUESDAY";
            case 3 -> "WEDNESDAY";
            case 4 -> "THURSDAY";
            case 5 -> "FRIDAY";
            case 6 -> "SATURDAY";
            case 7 -> "SUNDAY";
            default -> "UNKNOWN";
        };
        Double avg = (Double) best[1];
        return new BestDayResponse(dayName, avg);
    }

    public GoalCorrelationResponse getGoalCorrelation(Long userId, Long goalId) {
        LocalDateTime since = LocalDateTime.now().minusDays(30);
        LocalDateTime until = LocalDateTime.now();

        Goal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new RuntimeException("Goal not found with id: " + goalId));

        Double moodWith = journalEntryRepository.findAverageMoodForDaysWithActivity(userId, goal.getTargetActivity().getId(), since, until);
        Double moodWithout = journalEntryRepository.findAverageMoodForDaysWithoutActivity(userId, goal.getTargetActivity().getId(), since, until);

        Double improvement = (moodWith != null && moodWithout != null) ? moodWith - moodWithout : null;
        return new GoalCorrelationResponse(goal.getId(), goal.getTitle(), moodWith, moodWithout, improvement);
    }
}
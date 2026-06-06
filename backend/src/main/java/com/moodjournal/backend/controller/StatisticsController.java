package com.moodjournal.backend.controller;

import com.moodjournal.backend.dto.response.*;
import com.moodjournal.backend.security.JwtTokenProvider;
import com.moodjournal.backend.service.StatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;
    private final JwtTokenProvider tokenProvider;

    @GetMapping("/activity-correlation")
    public ResponseEntity<List<ActivityMoodStatsResponse>> getActivityCorrelation(
            @RequestParam(defaultValue = "30") int days,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getActivityMoodStats(userId, days));
    }

    @GetMapping("/weekly-trend")
    public ResponseEntity<List<WeeklyTrendResponse>> getWeeklyTrend(
            @RequestParam(defaultValue = "8") int weeks,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getWeeklyTrend(userId, weeks));
    }

    @GetMapping("/best-day")
    public ResponseEntity<BestDayResponse> getBestDay(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        BestDayResponse response = statisticsService.getBestDayOfWeek(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/goal-correlation/{goalId}")
    public ResponseEntity<GoalCorrelationResponse> getGoalCorrelation(
            @PathVariable Long goalId,
            HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        return ResponseEntity.ok(statisticsService.getGoalCorrelation(userId, goalId));
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        return tokenProvider.getUserIdFromToken(token);
    }

    private String extractToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }
}
package com.moodjournal.backend.controller;

import com.moodjournal.backend.dto.response.DailyMoodResponse;
import com.moodjournal.backend.security.JwtTokenProvider;
import com.moodjournal.backend.service.CalendarService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;
    private final JwtTokenProvider tokenProvider;

    @GetMapping("/{year}/{month}")
    public ResponseEntity<List<DailyMoodResponse>> getMonthlyCalendar(
            @PathVariable int year,
            @PathVariable int month,
            HttpServletRequest request) {
        Long userId = extractUserId(request);
        List<DailyMoodResponse> calendar = calendarService.getMonthlyMoods(userId, year, month);
        return ResponseEntity.ok(calendar);
    }

    private Long extractUserId(HttpServletRequest request) {
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
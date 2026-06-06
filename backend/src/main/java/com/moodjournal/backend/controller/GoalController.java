package com.moodjournal.backend.controller;

import com.moodjournal.backend.dto.request.CreateGoalRequest;
import com.moodjournal.backend.dto.response.GoalResponse;
import com.moodjournal.backend.security.JwtTokenProvider;
import com.moodjournal.backend.service.GoalService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(@Valid @RequestBody CreateGoalRequest request,
                                                   HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        GoalResponse response = goalService.createGoal(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> getUserGoals(HttpServletRequest httpRequest) {
        Long userId = getUserIdFromRequest(httpRequest);
        return ResponseEntity.ok(goalService.getUserGoals(userId));
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
        throw new RuntimeException("Missing token");
    }
}
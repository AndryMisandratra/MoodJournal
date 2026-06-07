package com.moodjournal.backend.controller;

import com.moodjournal.backend.dto.request.CreateActivityRequest;
import com.moodjournal.backend.dto.response.ActivityResponse;
import com.moodjournal.backend.security.JwtTokenProvider;
import com.moodjournal.backend.service.ActivityService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;
    private final JwtTokenProvider tokenProvider;

    @PostMapping
    public ResponseEntity<ActivityResponse> createActivity(@Valid @RequestBody CreateActivityRequest request,
                                                           HttpServletRequest httpReq) {
        Long userId = extractUserId(httpReq);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(activityService.createPersonalActivity(userId, request));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> getUserActivities(HttpServletRequest httpReq) {
        Long userId = extractUserId(httpReq);
        return ResponseEntity.ok(activityService.getUserActivities(userId));
    }

    @DeleteMapping("/{activityId}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long activityId,
                                               HttpServletRequest httpReq) {
        Long userId = extractUserId(httpReq);
        activityService.deleteActivity(userId, activityId);
        return ResponseEntity.noContent().build();
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
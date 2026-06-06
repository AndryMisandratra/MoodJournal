package com.moodjournal.backend.scheduler;

import com.moodjournal.backend.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GoalResetScheduler {
    private final GoalService goalService;

    @Scheduled(cron = "0 0 0 * * *") // chaque nuit à minuit
    public void resetMissedGoals() {
        goalService.resetStreaksForMissedDays();
    }
}
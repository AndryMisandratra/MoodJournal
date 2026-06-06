package com.moodjournal.backend.service;

import com.moodjournal.backend.dto.request.CreateGoalRequest;
import com.moodjournal.backend.dto.response.GoalResponse;
import com.moodjournal.backend.entity.Activity;
import com.moodjournal.backend.entity.Goal;
import com.moodjournal.backend.entity.GoalStatus;
import com.moodjournal.backend.entity.User;
import com.moodjournal.backend.repository.ActivityRepository;
import com.moodjournal.backend.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository goalRepository;
    private final ActivityRepository activityRepository;
    private final UserService userService;

    @Transactional
    public GoalResponse createGoal(Long userId, CreateGoalRequest request) {
        User user = userService.findById(userId);
        Activity target = activityRepository.findById(request.getTargetActivityId())
                .orElseThrow(() -> new RuntimeException("Activity not found"));

        Goal goal = Goal.builder()
                .user(user)
                .title(request.getTitle())
                .targetActivity(target)
                .currentStreak(0)
                .longestStreak(0)
                .status(GoalStatus.PENDING)
                .build();
        goal = goalRepository.save(goal);
        return mapToResponse(goal);
    }

    @Transactional(readOnly = true)
    public List<GoalResponse> getUserGoals(Long userId) {
        User user = userService.findById(userId);
        return goalRepository.findByUserAndStatus(user, GoalStatus.PENDING) // ou tous les statuts
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateStreakForActivity(Long userId, Long activityId, LocalDateTime entryDate) {
        // Récupérer l'objectif associé à cette activité pour cet utilisateur
        User user = userService.findById(userId);
        goalRepository.findByUserAndTargetActivityId(user, activityId).ifPresent(goal -> {
            // Vérifier si la dernière complétion date d'aujourd'hui
            LocalDateTime lastCompleted = goal.getLastCompletedAt();
            LocalDateTime todayStart = entryDate.toLocalDate().atStartOfDay();
            if (lastCompleted == null || lastCompleted.isBefore(todayStart)) {
                // Incrémenter le streak
                goal.incrementStreak();
            }
            // Si l'entrée est pour le même jour, ne pas incrémenter deux fois (déjà fait)
            goalRepository.save(goal);
        });
    }

    // À appeler par un scheduler chaque nuit pour réinitialiser les streaks non complétés
    @Transactional
    public void resetStreaksForMissedDays() {
        LocalDateTime yesterdayEnd = LocalDateTime.now().minusDays(1).toLocalDate().atTime(23, 59, 59);
        // Récupérer tous les objectifs PENDING ou COMPLETED dont la dernière complétion est antérieure à aujourd'hui
        List<Goal> goals = goalRepository.findAll(); // simplifié, à optimiser
        for (Goal goal : goals) {
            if (goal.getStatus() == GoalStatus.COMPLETED && goal.getLastCompletedAt().toLocalDate().isBefore(LocalDate.now())) {
                goal.resetStreak();
                goalRepository.save(goal);
            }
        }
    }

    private GoalResponse mapToResponse(Goal goal) {
        return new GoalResponse(
                goal.getId(),
                goal.getTitle(),
                goal.getTargetActivity().getId(),
                goal.getTargetActivity().getName(),
                goal.getCurrentStreak(),
                goal.getLongestStreak(),
                goal.getStatus(),
                goal.getLastCompletedAt()
        );
    }

}
package com.moodjournal.backend.repository;

import com.moodjournal.backend.entity.Goal;
import com.moodjournal.backend.entity.GoalStatus;
import com.moodjournal.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    List<Goal> findByUserAndStatus(User user, GoalStatus status);
    Optional<Goal> findByUserAndTargetActivityId(User user, Long activityId);
}
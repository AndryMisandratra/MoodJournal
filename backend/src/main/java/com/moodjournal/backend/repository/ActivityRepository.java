package com.moodjournal.backend.repository;

import com.moodjournal.backend.entity.Activity;
import com.moodjournal.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByUserIsNullOrUser(User user);
}
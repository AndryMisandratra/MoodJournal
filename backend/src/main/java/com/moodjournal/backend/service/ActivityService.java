package com.moodjournal.backend.service;

import com.moodjournal.backend.dto.request.CreateActivityRequest;
import com.moodjournal.backend.dto.response.ActivityResponse;
import com.moodjournal.backend.entity.Activity;
import com.moodjournal.backend.entity.User;
import com.moodjournal.backend.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final UserService userService;

    public ActivityResponse createPersonalActivity(Long userId, CreateActivityRequest request) {
        User user = userService.findById(userId);
        Activity activity = Activity.builder()
                .name(request.getName())
                .icon(request.getIcon())
                .color(request.getColor())
                .user(user)
                .global(false)
                .build();
        activity = activityRepository.save(activity);
        return mapToResponse(activity);
    }

    public List<ActivityResponse> getUserActivities(Long userId) {
        User user = userService.findById(userId);
        List<Activity> activities = activityRepository.findByUserIsNullOrUser(user);
        return activities.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public void deleteActivity(Long userId, Long activityId) {
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found"));
        // Vérifier que l'activité appartient bien à l'utilisateur et n'est pas globale
        if (activity.getGlobal() || !activity.getUser().getId().equals(userId)) {
            throw new RuntimeException("Cannot delete this activity");
        }
        activityRepository.delete(activity);
    }

    private ActivityResponse mapToResponse(Activity a) {
        return new ActivityResponse(a.getId(), a.getName(), a.getIcon(), a.getColor());
    }
}
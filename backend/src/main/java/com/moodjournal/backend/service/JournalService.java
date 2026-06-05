package com.moodjournal.backend.service;

import com.moodjournal.backend.dto.request.CreateEntryRequest;
import com.moodjournal.backend.dto.request.UpdateEntryRequest;
import com.moodjournal.backend.dto.response.EntryResponse.ActivitySummary;
import com.moodjournal.backend.dto.response.EntryResponse;
import com.moodjournal.backend.entity.Activity;
import com.moodjournal.backend.entity.JournalEntry;
import com.moodjournal.backend.entity.User;
import com.moodjournal.backend.repository.ActivityRepository;
import com.moodjournal.backend.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JournalService {

    private final JournalEntryRepository entryRepository;
    private final ActivityRepository activityRepository;
    private final UserService userService; // réutiliser votre service existant

    @Transactional
    public EntryResponse createEntry(Long userId, CreateEntryRequest request) {
        User user = userService.findByEmail(userId.toString());

        JournalEntry entry = JournalEntry.builder()
                .user(user)
                .mood(request.getMood())
                .note(request.getNote())
                .build();

        if (request.getActivityIds() != null && !request.getActivityIds().isEmpty()) {
            List<Activity> activities = activityRepository.findAllById(request.getActivityIds());
            entry.setActivities(activities);
        }

        entry = entryRepository.save(entry);

        return mapToResponse(entry);
    }

    private EntryResponse mapToResponse(JournalEntry entry) {
        List<ActivitySummary> summaries = entry.getActivities().stream()
                .map(a -> new ActivitySummary(a.getId(), a.getName(), a.getIcon(), a.getColor()))
                .collect(Collectors.toList());
        return new EntryResponse(entry.getId(), entry.getMood(), entry.getNote(),
                entry.getRecordedAt(), summaries);
    }

    public List<EntryResponse> getUserEntries(Long userId) {
        List<JournalEntry> entries = entryRepository.findByUserIdOrderByRecordedAtDesc(userId);
        return entries.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public EntryResponse updateEntry(Long userId, Long entryId, UpdateEntryRequest request) {
        JournalEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        if (!entry.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to modify this entry");
        }
        if (request.getMood() != null) {
            entry.setMood(request.getMood());
        }
        if (request.getNote() != null) {
            entry.setNote(request.getNote());
        }
        if (request.getActivityIds() != null) {
            List<Activity> activities = activityRepository.findAllById(request.getActivityIds());
            entry.setActivities(activities);
        }
        entry = entryRepository.save(entry);
        return mapToResponse(entry);
    }

    @Transactional
    public void deleteEntry(Long userId, Long entryId) {
        JournalEntry entry = entryRepository.findById(entryId)
                .orElseThrow(() -> new RuntimeException("Entry not found"));
        if (!entry.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized to delete this entry");
        }
        entryRepository.delete(entry);
    }


}
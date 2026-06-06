package com.moodjournal.backend.service;

import com.moodjournal.backend.dto.response.DailyMoodResponse;
import com.moodjournal.backend.entity.JournalEntry;
import com.moodjournal.backend.entity.MoodType;
import com.moodjournal.backend.repository.JournalEntryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final JournalEntryRepository journalEntryRepository;

    public List<DailyMoodResponse> getMonthlyMoods(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59);

        // 1. Récupérer toutes les entrées du mois
        List<JournalEntry> entries = journalEntryRepository
                .findByUserIdAndRecordedAtBetweenOrderByRecordedAtAsc(userId, startDateTime, endDateTime);

        // 2. Grouper par jour : pour chaque jour, on garde la dernière humeur (celle du dernier enregistrement)
        Map<LocalDate, MoodType> moodByDay = entries.stream()
                .collect(Collectors.toMap(
                        e -> e.getRecordedAt().toLocalDate(),
                        JournalEntry::getMood,
                        (mood1, mood2) -> mood2   // si plusieurs entrées le même jour, on prend la plus récente
                ));

        // 3. Générer la réponse pour tous les jours du mois
        List<DailyMoodResponse> result = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            result.add(new DailyMoodResponse(date, moodByDay.get(date)));
        }
        return result;
    }
}
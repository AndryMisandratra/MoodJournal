package com.moodjournal.backend.repository;

import com.moodjournal.backend.entity.JournalEntry;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserIdOrderByRecordedAtDesc(Long userId);
    Page<JournalEntry> findByUserId(Long userId, Pageable pageable);
    List<JournalEntry> findByUserIdAndRecordedAtBetweenOrderByRecordedAtAsc(Long userId, LocalDateTime start, LocalDateTime end);

    // Moyenne d'humeur par activité (JPQL)
    @Query("SELECT a.id, a.name, a.icon, a.color, " +
            "AVG(CASE WHEN je.mood = 'VERY_GOOD' THEN 5 WHEN je.mood = 'GOOD' THEN 4 WHEN je.mood = 'NEUTRAL' THEN 3 WHEN je.mood = 'BAD' THEN 2 ELSE 1 END), " +
            "COUNT(je) " +
            "FROM JournalEntry je JOIN je.activities a " +
            "WHERE je.user.id = :userId AND je.recordedAt >= :since " +
            "GROUP BY a.id, a.name, a.icon, a.color")
    List<Object[]> findAverageMoodByActivitySince(@Param("userId") Long userId, @Param("since") LocalDateTime since);

    // Évolution hebdomadaire (JPQL avec fonction DATE_TRUNC)
    @Query("SELECT FUNCTION('DATE_TRUNC', 'week', je.recordedAt) as weekStart, " +
            "AVG(CASE WHEN je.mood = 'VERY_GOOD' THEN 5 WHEN je.mood = 'GOOD' THEN 4 WHEN je.mood = 'NEUTRAL' THEN 3 WHEN je.mood = 'BAD' THEN 2 ELSE 1 END) " +
            "FROM JournalEntry je WHERE je.user.id = :userId " +
            "GROUP BY weekStart ORDER BY weekStart DESC")
    List<Object[]> findWeeklyAverageMood(@Param("userId") Long userId, Pageable pageable);

    // Meilleur jour de la semaine (utilisation de EXTRACT en SQL natif pour éviter les problèmes de fonction)
    @Query(value = "SELECT EXTRACT(DOW FROM recorded_at) as dow, " +
            "AVG(CASE mood WHEN 'VERY_GOOD' THEN 5 WHEN 'GOOD' THEN 4 WHEN 'NEUTRAL' THEN 3 WHEN 'BAD' THEN 2 ELSE 1 END) " +
            "FROM journal_entries WHERE user_id = :userId GROUP BY dow ORDER BY avg DESC LIMIT 1", nativeQuery = true)
    List<Object[]> findBestDayOfWeek(@Param("userId") Long userId);

    // Moyenne d'humeur pour les jours avec une activité donnée
    @Query("SELECT AVG(CASE WHEN je.mood = 'VERY_GOOD' THEN 5 WHEN je.mood = 'GOOD' THEN 4 WHEN je.mood = 'NEUTRAL' THEN 3 WHEN je.mood = 'BAD' THEN 2 ELSE 1 END) " +
            "FROM JournalEntry je WHERE je.user.id = :userId " +
            "AND je.recordedAt BETWEEN :since AND :until " +
            "AND EXISTS (SELECT a FROM je.activities a WHERE a.id = :activityId)")
    Double findAverageMoodForDaysWithActivity(@Param("userId") Long userId, @Param("activityId") Long activityId,
                                              @Param("since") LocalDateTime since, @Param("until") LocalDateTime until);

    // Moyenne d'humeur pour les jours sans cette activité
    @Query("SELECT AVG(CASE WHEN je.mood = 'VERY_GOOD' THEN 5 WHEN je.mood = 'GOOD' THEN 4 WHEN je.mood = 'NEUTRAL' THEN 3 WHEN je.mood = 'BAD' THEN 2 ELSE 1 END) " +
            "FROM JournalEntry je WHERE je.user.id = :userId " +
            "AND je.recordedAt BETWEEN :since AND :until " +
            "AND NOT EXISTS (SELECT a FROM je.activities a WHERE a.id = :activityId)")
    Double findAverageMoodForDaysWithoutActivity(@Param("userId") Long userId, @Param("activityId") Long activityId,
                                                 @Param("since") LocalDateTime since, @Param("until") LocalDateTime until);
}


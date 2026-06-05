package com.moodjournal.backend.repository;

import com.moodjournal.backend.entity.JournalEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JournalEntryRepository extends JpaRepository<JournalEntry, Long> {
    List<JournalEntry> findByUserIdOrderByRecordedAtDesc(Long userId);
    Page<JournalEntry> findByUserId(Long userId, Pageable pageable);
}

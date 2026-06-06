package com.moodjournal.backend.entity;

public enum GoalStatus {
    INACTIVE,   // objectif désactivé
    PENDING,    // actif, pas encore complété aujourd'hui
    COMPLETED,  // complété aujourd'hui
    FAILED      // non complété hier (utile pour notifier)
}
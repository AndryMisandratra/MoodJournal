package com.moodjournal.backend.entity;

public enum MoodType {
    VERY_GOOD(5, "🤩"),
    GOOD(4, "🙂"),
    NEUTRAL(3, "😐"),
    BAD(2, "🙁"),
    VERY_BAD(1, "😢");

    private final int score;
    private final String emoji;

    MoodType(int score, String emoji) {
        this.score = score;
        this.emoji = emoji;
    }
    public int getScore() { return score; }
    public String getEmoji() { return emoji; }
}
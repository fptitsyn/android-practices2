package ru.mirea.ptitsyn.domain.models;

public final class RecognitionResult {
    private final String label;
    private final String word;
    private final float score;

    public RecognitionResult(String label, String word, float score) {
        this.label = label;
        this.word = word;
        this.score = score;
    }

    public String getLabel() {
        return label;
    }

    public String getWord() {
        return word;
    }

    public float getScore() {
        return score;
    }
}
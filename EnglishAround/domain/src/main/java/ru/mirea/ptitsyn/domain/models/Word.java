package ru.mirea.ptitsyn.domain.models;

public final class Word {
    private final int id;
    private final String english;
    private final String translation;

    public Word(int id, String english, String translation) {
        this.id = id;
        this.english = english;
        this.translation = translation;
    }

    public int getId() {
        return id;
    }

    public String getEnglish() {
        return english;
    }

    public String getTranslation() {
        return translation;
    }
}
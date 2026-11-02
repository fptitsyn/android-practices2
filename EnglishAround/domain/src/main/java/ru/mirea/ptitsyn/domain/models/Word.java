package ru.mirea.ptitsyn.domain.models;

public final class Word {
    private final int id;
    private final String english;
    private final String translation;
    private final String imageUrl;

    public Word(int id, String english, String translation) {
        this(id, english, translation, "");
    }

    public Word(int id, String english, String translation, String imageUrl) {
        this.id = id;
        this.english = english;
        this.translation = translation;
        this.imageUrl = imageUrl == null ? "" : imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }
}
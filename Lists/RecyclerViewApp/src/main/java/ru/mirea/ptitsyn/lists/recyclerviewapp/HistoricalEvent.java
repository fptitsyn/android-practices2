package ru.mirea.ptitsyn.lists.recyclerviewapp;



public final class HistoricalEvent {
    private final String date;
    private final String title;
    private final String description;
    private final int imageResId;

    public HistoricalEvent(String date, String title, String description, int imageResId) {
        this.date = date;
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
    }

    public String getDate() { return date; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
}

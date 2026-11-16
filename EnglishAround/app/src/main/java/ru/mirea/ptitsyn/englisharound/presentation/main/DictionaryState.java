package ru.mirea.ptitsyn.englisharound.presentation.main;

import ru.mirea.ptitsyn.domain.models.WordDetails;

public final class DictionaryState {
    private final String word;
    private final boolean loading;
    private final WordDetails details;
    private final String error;

    public DictionaryState(String word, boolean loading, WordDetails details, String error) {
        this.word = word;
        this.loading = loading;
        this.details = details;
        this.error = error;
    }

    public String getWord() {
        return word;
    }

    public boolean isLoading() {
        return loading;
    }

    public WordDetails getDetails() {
        return details;
    }

    public String getError() {
        return error;
    }
}
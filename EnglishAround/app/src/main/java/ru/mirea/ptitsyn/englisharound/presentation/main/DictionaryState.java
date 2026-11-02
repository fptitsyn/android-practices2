package ru.mirea.ptitsyn.englisharound.presentation.main;

import ru.mirea.ptitsyn.domain.models.WordDetails;

public final class DictionaryState {
    private final boolean loading;
    private final WordDetails details;
    private final String error;

    public DictionaryState(boolean loading, WordDetails details, String error) {
        this.loading = loading;
        this.details = details;
        this.error = error;
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
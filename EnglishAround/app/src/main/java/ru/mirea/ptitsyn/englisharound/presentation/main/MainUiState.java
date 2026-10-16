package ru.mirea.ptitsyn.englisharound.presentation.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.models.Word;

public final class MainUiState {
    private final User user;
    private final List<Word> catalog;
    private final List<Word> dictionary;
    private final boolean loading;
    private final boolean signedOut;
    private final String error;

    public MainUiState(
            User user,
            List<Word> catalog,
            List<Word> dictionary,
            boolean loading,
            boolean signedOut,
            String error
    ) {
        this.user = user;

        this.catalog = Collections.unmodifiableList(
                new ArrayList<>(catalog)
        );

        this.dictionary = Collections.unmodifiableList(
                new ArrayList<>(dictionary)
        );

        this.loading = loading;
        this.signedOut = signedOut;
        this.error = error;
    }

    public User getUser() {
        return user;
    }

    public List<Word> getCatalog() {
        return catalog;
    }

    public List<Word> getDictionary() {
        return dictionary;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isSignedOut() {
        return signedOut;
    }

    public String getError() {
        return error;
    }
}
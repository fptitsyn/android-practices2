package ru.mirea.ptitsyn.englisharound.presentation.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.models.Word;

public final class MainUiState {

    private final User user;
    private final List<Word> catalog;
    private final List<Word> dictionary;

    private final List<String> catalogRows;
    private final List<String> dictionaryRows;

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

        this.catalog = List.copyOf(catalog);

        this.dictionary = List.copyOf(dictionary);

        this.loading = loading;
        this.signedOut = signedOut;
        this.error = error;

        Set<Integer> savedIds = new HashSet<>();
        List<String> dictionaryText = new ArrayList<>();

        for (Word word : dictionary) {
            savedIds.add(word.getId());
            dictionaryText.add(formatWord(word));
        }

        List<String> catalogText = new ArrayList<>();

        for (Word word : catalog) {
            String row = formatWord(word);

            if (savedIds.contains(word.getId())) {
                row += "  ✓";
            }

            catalogText.add(row);
        }

        catalogRows = List.copyOf(catalogText);
        dictionaryRows = List.copyOf(dictionaryText);
    }

    private static String formatWord(Word word) {
        return word.getEnglish() + " — " + word.getTranslation();
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

    public List<String> getCatalogRows() {
        return catalogRows;
    }

    public List<String> getDictionaryRows() {
        return dictionaryRows;
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
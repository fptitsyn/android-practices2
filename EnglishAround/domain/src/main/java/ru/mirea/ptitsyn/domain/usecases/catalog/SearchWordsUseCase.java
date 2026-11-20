package ru.mirea.ptitsyn.domain.usecases.catalog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ru.mirea.ptitsyn.domain.models.Word;

public final class SearchWordsUseCase {

    public List<Word> execute(List<Word> words, String query) {
        String filter = query.trim().toLowerCase(Locale.ROOT);
        if (filter.isEmpty()) {
            return words;
        }

        List<Word> result = new ArrayList<>();
        for (Word word : words) {
            String english = word.getEnglish().toLowerCase(Locale.ROOT);
            String translation = word.getTranslation().toLowerCase(Locale.ROOT);
            if (english.contains(filter) || translation.contains(filter)) {
                result.add(word);
            }
        }

        return result;
    }
}
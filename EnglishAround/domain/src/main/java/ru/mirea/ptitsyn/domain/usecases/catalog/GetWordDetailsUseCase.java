package ru.mirea.ptitsyn.domain.usecases.catalog;

import java.util.Locale;

import ru.mirea.ptitsyn.domain.models.WordDetails;
import ru.mirea.ptitsyn.domain.repository.WordRepository;

public final class GetWordDetailsUseCase {
    private final WordRepository repository;

    public GetWordDetailsUseCase(WordRepository repository) {
        this.repository = repository;
    }

    public WordDetails execute(String word) {
        if (word == null || word.trim().isEmpty()) {
            throw new IllegalArgumentException("Введите английское слово");
        }

        return repository.getWordDetails(word.trim().toLowerCase(Locale.ROOT));
    }
}
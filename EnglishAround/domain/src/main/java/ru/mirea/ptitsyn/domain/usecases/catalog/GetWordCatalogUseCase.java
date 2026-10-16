package ru.mirea.ptitsyn.domain.usecases.catalog;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.repository.WordRepository;

public final class GetWordCatalogUseCase {
    private final WordRepository repository;

    public GetWordCatalogUseCase(WordRepository repository) {
        this.repository = repository;
    }

    public List<Word> execute() {
        return repository.getWords();
    }
}
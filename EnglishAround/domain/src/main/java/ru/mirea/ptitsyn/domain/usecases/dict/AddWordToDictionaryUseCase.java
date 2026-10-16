package ru.mirea.ptitsyn.domain.usecases.dict;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.repository.UserDictionaryRepository;

public final class AddWordToDictionaryUseCase {
    private final UserDictionaryRepository repository;

    public AddWordToDictionaryUseCase(
            UserDictionaryRepository repository
    ) {
        this.repository = repository;
    }

    public void execute(String userId, Word word) {
        repository.addWord(userId, word);
    }
}
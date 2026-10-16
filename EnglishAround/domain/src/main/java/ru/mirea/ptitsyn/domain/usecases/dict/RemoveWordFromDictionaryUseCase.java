package ru.mirea.ptitsyn.domain.usecases.dict;

import ru.mirea.ptitsyn.domain.repository.UserDictionaryRepository;

public final class RemoveWordFromDictionaryUseCase {
    private final UserDictionaryRepository repository;

    public RemoveWordFromDictionaryUseCase(
            UserDictionaryRepository repository
    ) {
        this.repository = repository;
    }

    public void execute(String userId, int wordId) {
        repository.removeWord(userId, wordId);
    }
}
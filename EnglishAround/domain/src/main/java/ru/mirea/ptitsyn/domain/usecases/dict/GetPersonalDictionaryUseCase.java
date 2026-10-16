package ru.mirea.ptitsyn.domain.usecases.dict;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.repository.UserDictionaryRepository;

public final class GetPersonalDictionaryUseCase {
    private final UserDictionaryRepository repository;

    public GetPersonalDictionaryUseCase(
            UserDictionaryRepository repository
    ) {
        this.repository = repository;
    }

    public List<Word> execute(String userId) {
        return repository.getWords(userId);
    }
}
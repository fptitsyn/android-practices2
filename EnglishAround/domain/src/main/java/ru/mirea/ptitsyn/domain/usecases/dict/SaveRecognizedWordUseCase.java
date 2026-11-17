package ru.mirea.ptitsyn.domain.usecases.dict;

import ru.mirea.ptitsyn.domain.repository.UserDictionaryRepository;

public final class SaveRecognizedWordUseCase {
    private final UserDictionaryRepository repository;

    public SaveRecognizedWordUseCase(UserDictionaryRepository repository) {
        this.repository = repository;
    }

    public void execute(String userId, String english, String translation) {
        if (english == null || english.trim().isEmpty()) {
            throw new IllegalArgumentException("Введите английское слово");
        }

        if (translation == null || translation.trim().isEmpty()) {
            throw new IllegalArgumentException("Введите перевод");
        }

        repository.saveRecognizedWord(userId, english, translation);
    }
}
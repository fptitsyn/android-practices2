package ru.mirea.ptitsyn.domain.repository;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;

public interface UserDictionaryRepository {
    void addWord(String userId, Word word);

    List<Word> getWords(String userId);

    void removeWord(String userId, int wordId);
}
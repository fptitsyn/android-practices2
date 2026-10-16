package ru.mirea.ptitsyn.domain.repository;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;

public interface WordRepository {
    List<Word> getWords();
}

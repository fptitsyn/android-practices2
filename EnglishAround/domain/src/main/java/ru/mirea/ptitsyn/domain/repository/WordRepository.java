package ru.mirea.ptitsyn.domain.repository;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.models.WordDetails;

public interface WordRepository {
    List<Word> getWords();

    WordDetails getWordDetails(String word);
}
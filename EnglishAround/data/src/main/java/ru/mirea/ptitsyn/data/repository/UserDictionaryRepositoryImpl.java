package ru.mirea.ptitsyn.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ptitsyn.data.database.DictionaryWordDao;
import ru.mirea.ptitsyn.data.database.DictionaryWordEntity;
import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.repository.UserDictionaryRepository;

public final class UserDictionaryRepositoryImpl
        implements UserDictionaryRepository {

    private final DictionaryWordDao dao;

    public UserDictionaryRepositoryImpl(DictionaryWordDao dao) {
        this.dao = dao;
    }

    @Override
    public void addWord(String userId, Word word) {
        dao.insert(new DictionaryWordEntity(
                userId,
                word.getId(),
                word.getEnglish(),
                word.getTranslation()
        ));
    }

    @Override
    public List<Word> getWords(String userId) {
        List<Word> words = new ArrayList<>();

        for (DictionaryWordEntity entity : dao.getWords(userId)) {
            words.add(new Word(
                    entity.wordId,
                    entity.english,
                    entity.translation
            ));
        }

        return words;
    }

    @Override
    public void removeWord(String userId, int wordId) {
        dao.delete(userId, wordId);
    }
}
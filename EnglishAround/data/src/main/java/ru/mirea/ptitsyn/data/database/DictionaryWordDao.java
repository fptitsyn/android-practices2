package ru.mirea.ptitsyn.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface DictionaryWordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DictionaryWordEntity word);

    @Query("SELECT * FROM dictionary_words WHERE userId = :userId ORDER BY english COLLATE NOCASE")
    List<DictionaryWordEntity> getWords(String userId);

    @Query("DELETE FROM dictionary_words WHERE userId = :userId AND wordId = :wordId")
    void delete(String userId, int wordId);

    @Query("SELECT * FROM dictionary_words WHERE userId = :userId AND LOWER(TRIM(english)) = :english LIMIT 1")
    DictionaryWordEntity findByEnglish(String userId, String english);

    @Query("SELECT COALESCE(MIN(wordId), 0) FROM dictionary_words WHERE userId = :userId")
    int getMinimumId(String userId);

    @Transaction
    default void saveRecognizedWord(String userId, String english, String translation) {
        DictionaryWordEntity existing = findByEnglish(userId, english);
        int id;

        if (existing != null) {
            id = existing.wordId;
        } else {
            int minimum = Math.min(0, getMinimumId(userId));

            if (minimum == Integer.MIN_VALUE) {
                throw new IllegalStateException("Закончились доступные идентификаторы");
            }

            id = minimum - 1;
        }

        insert(new DictionaryWordEntity(userId, id, english, translation));
    }
}
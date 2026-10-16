package ru.mirea.ptitsyn.data.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DictionaryWordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DictionaryWordEntity word);

    @Query(
            "SELECT * FROM dictionary_words " +
                    "WHERE userId = :userId ORDER BY english COLLATE NOCASE"
    )
    List<DictionaryWordEntity> getWords(String userId);

    @Query(
            "DELETE FROM dictionary_words " +
                    "WHERE userId = :userId AND wordId = :wordId"
    )
    void delete(String userId, int wordId);
}
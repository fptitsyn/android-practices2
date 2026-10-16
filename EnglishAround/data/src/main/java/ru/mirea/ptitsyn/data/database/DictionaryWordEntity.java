package ru.mirea.ptitsyn.data.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(
        tableName = "dictionary_words",
        primaryKeys = {"userId", "wordId"}
)
public class DictionaryWordEntity {
    @NonNull
    public String userId;

    public int wordId;
    public String english;
    public String translation;

    public DictionaryWordEntity(
            @NonNull String userId,
            int wordId,
            String english,
            String translation
    ) {
        this.userId = userId;
        this.wordId = wordId;
        this.english = english;
        this.translation = translation;
    }
}
package ru.mirea.ptitsyn.data.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.repository.WordRepository;

public final class WordRepositoryImpl implements WordRepository {

    @Override
    public List<Word> getWords() {
        // Заглушка каталога: данные, которые позднее будут приходить от внешнего API.
        return new ArrayList<>(Arrays.asList(
                new Word(1, "apple", "яблоко"),
                new Word(2, "book", "книга"),
                new Word(3, "cup", "чашка"),
                new Word(4, "chair", "стул"),
                new Word(5, "window", "окно"),
                new Word(6, "door", "дверь"),
                new Word(7, "table", "стол"),
                new Word(8, "phone", "телефон"),
                new Word(9, "computer", "компьютер"),
                new Word(10, "bag", "сумка"),
                new Word(11, "pen", "ручка"),
                new Word(12, "pencil", "карандаш"),
                new Word(13, "bottle", "бутылка"),
                new Word(14, "clock", "часы"),
                new Word(15, "lamp", "лампа"),
                new Word(16, "mirror", "зеркало"),
                new Word(17, "plate", "тарелка"),
                new Word(18, "spoon", "ложка"),
                new Word(19, "fork", "вилка"),
                new Word(20, "key", "ключ")
        ));
    }
}
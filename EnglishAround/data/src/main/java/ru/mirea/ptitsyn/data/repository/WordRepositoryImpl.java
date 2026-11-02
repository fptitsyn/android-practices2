package ru.mirea.ptitsyn.data.repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ru.mirea.ptitsyn.data.network.DictionaryEntryDto;
import ru.mirea.ptitsyn.data.network.NetworkApi;
import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.models.WordDetails;
import ru.mirea.ptitsyn.domain.repository.RepositoryException;
import ru.mirea.ptitsyn.domain.repository.WordRepository;

public final class WordRepositoryImpl implements WordRepository {
    private final NetworkApi networkApi;

    public WordRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<Word> getWords() {
        return new ArrayList<>(Arrays.asList(
                createWord(1, "apple", "яблоко"),
                createWord(2, "book", "книга"),
                createWord(3, "cup", "чашка"),
                createWord(4, "chair", "стул"),
                createWord(5, "window", "окно"),
                createWord(6, "door", "дверь"),
                createWord(7, "table", "стол"),
                createWord(8, "phone", "телефон"),
                createWord(9, "computer", "компьютер"),
                createWord(10, "bag", "сумка"),
                createWord(11, "pen", "ручка"),
                createWord(12, "pencil", "карандаш"),
                createWord(13, "bottle", "бутылка"),
                createWord(14, "clock", "часы"),
                createWord(15, "lamp", "лампа"),
                createWord(16, "mirror", "зеркало"),
                createWord(17, "plate", "тарелка"),
                createWord(18, "spoon", "ложка"),
                createWord(19, "fork", "вилка"),
                createWord(20, "key", "ключ")
        ));
    }

    @Override
    public WordDetails getWordDetails(String word) {
        List<DictionaryEntryDto> entries = networkApi.getWord(word);

        for (DictionaryEntryDto entry : entries) {
            if (entry == null || entry.getMeanings() == null) {
                continue;
            }

            for (DictionaryEntryDto.MeaningDto meaning : entry.getMeanings()) {
                if (meaning == null || meaning.getDefinitions() == null) {
                    continue;
                }

                for (DictionaryEntryDto.DefinitionDto definition : meaning.getDefinitions()) {
                    if (definition == null || empty(definition.getDefinition())) {
                        continue;
                    }

                    String resultWord = empty(entry.getWord()) ? word : entry.getWord();

                    return new WordDetails(
                            resultWord,
                            getPhonetic(entry),
                            valueOrEmpty(meaning.getPartOfSpeech()),
                            definition.getDefinition(),
                            valueOrEmpty(definition.getExample())
                    );
                }
            }
        }

        throw new RepositoryException("Для этого слова не найдено доступное определение");
    }

    private Word createWord(int id, String english, String translation) {
        String imageUrl = "https://placehold.co/400x300/png?text=" + english;
        return new Word(id, english, translation, imageUrl);
    }

    private String getPhonetic(DictionaryEntryDto entry) {
        if (!empty(entry.getPhonetic())) {
            return entry.getPhonetic();
        }

        if (entry.getPhonetics() != null) {
            for (DictionaryEntryDto.PhoneticDto phonetic : entry.getPhonetics()) {
                if (phonetic != null && !empty(phonetic.getText())) {
                    return phonetic.getText();
                }
            }
        }

        return "";
    }

    private boolean empty(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }
}
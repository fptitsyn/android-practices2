package ru.mirea.ptitsyn.data.repository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ptitsyn.data.network.NetworkApi;
import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.domain.repository.WordRepository;

public final class WordRepositoryImpl implements WordRepository {
    private final NetworkApi networkApi;

    public WordRepositoryImpl(NetworkApi networkApi) {
        this.networkApi = networkApi;
    }

    @Override
    public List<Word> getWords() {
        try {
            JSONArray array = new JSONArray(networkApi.getWordsJson());
            List<Word> words = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                JSONObject item = array.getJSONObject(i);

                words.add(new Word(
                        item.getInt("id"),
                        item.getString("english"),
                        item.getString("translation")
                ));
            }

            return words;
        } catch (JSONException exception) {
            throw new IllegalStateException(
                    "Некорректный JSON каталога слов",
                    exception
            );
        }
    }
}
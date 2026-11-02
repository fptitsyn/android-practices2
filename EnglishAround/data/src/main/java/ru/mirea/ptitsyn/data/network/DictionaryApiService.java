package ru.mirea.ptitsyn.data.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface DictionaryApiService {

    @GET("entries/en/{word}")
    Call<List<DictionaryEntryDto>> getWord(@Path("word") String word);
}
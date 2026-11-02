package ru.mirea.ptitsyn.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class ApiClient {
    private static final String BASE_URL = "https://api.dictionaryapi.dev/api/v2/";

    private ApiClient() {
    }

    public static DictionaryApiService createDictionaryService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();

        return retrofit.create(DictionaryApiService.class);
    }
}
package ru.mirea.ptitsyn.lesson9.data.storage.sharedprefs;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.ptitsyn.lesson9.data.storage.MovieStorage;
import ru.mirea.ptitsyn.lesson9.data.storage.models.MovieModel;

public final class SharedPrefMovieStorage implements MovieStorage {
    private static final String SHARED_PREFS_NAME = "movie_preferences";
    private static final String ID_KEY = "favorite_movie_id";
    private static final String NAME_KEY = "favorite_movie_name";
    private static final String DATE_KEY = "favorite_movie_date";

    private final SharedPreferences preferences;

    public SharedPrefMovieStorage(Context context) {
        preferences = context.getApplicationContext().getSharedPreferences(
                SHARED_PREFS_NAME, Context.MODE_PRIVATE
        );
    }

    @Override
    public MovieModel get() {
        String name = preferences.getString(NAME_KEY, null);
        if (name == null) {
            return null;
        }
        return new MovieModel(
                preferences.getInt(ID_KEY, -1),
                name,
                preferences.getString(DATE_KEY, "")
        );
    }

    @Override
    public boolean save(MovieModel movieModel) {
        return preferences.edit()
                .putInt(ID_KEY, movieModel.getId())
                .putString(NAME_KEY, movieModel.getName())
                .putString(DATE_KEY, movieModel.getLocalDate())
                .commit();
    }
}

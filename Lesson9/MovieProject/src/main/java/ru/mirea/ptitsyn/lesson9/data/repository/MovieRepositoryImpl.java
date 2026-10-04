package ru.mirea.ptitsyn.lesson9.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.ptitsyn.lesson9.domain.models.Movie;
import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;

public class MovieRepositoryImpl implements MovieRepository {

    private static final String PREFERENCES_NAME = "movie_preferences";
    private static final String KEY_MOVIE_ID = "favorite_movie_id";
    private static final String KEY_MOVIE_NAME = "favorite_movie_name";

    private final SharedPreferences sharedPreferences;

    public MovieRepositoryImpl(Context context) {
        // ApplicationContext не связан с жизненным циклом Activity
        Context applicationContext = context.getApplicationContext();

        sharedPreferences = applicationContext.getSharedPreferences(
                PREFERENCES_NAME,
                Context.MODE_PRIVATE
        );
    }

    @Override
    public boolean saveMovie(Movie movie) {
        if (movie == null || movie.getName() == null) {
            return false;
        }

        sharedPreferences.edit()
                .putInt(KEY_MOVIE_ID, movie.getId())
                .putString(KEY_MOVIE_NAME, movie.getName())
                .apply();

        return true;
    }

    @Override
    public Movie getMovie() {
        int movieId = sharedPreferences.getInt(KEY_MOVIE_ID, -1);
        String movieName = sharedPreferences.getString(
                KEY_MOVIE_NAME,
                null
        );

        if (movieName == null) {
            return null;
        }

        return new Movie(movieId, movieName);
    }
}
package ru.mirea.ptitsyn.lesson9.presentation;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.ptitsyn.lesson9.domain.GetFavoriteFilmUseCase;
import ru.mirea.ptitsyn.lesson9.domain.SaveMovieToFavoriteUseCase;
import ru.mirea.ptitsyn.lesson9.domain.models.Movie;
import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;

public final class MainViewModel extends ViewModel {
    private final GetFavoriteFilmUseCase getFavoriteFilm;
    private final SaveMovieToFavoriteUseCase saveMovieToFavorite;
    private final MutableLiveData<String> favoriteMovie =
            new MutableLiveData<>("Нет данных!");

    public MainViewModel(MovieRepository movieRepository) {
        getFavoriteFilm = new GetFavoriteFilmUseCase(movieRepository);
        saveMovieToFavorite = new SaveMovieToFavoriteUseCase(movieRepository);
        Log.d("MainViewModel", "MainViewModel created");
    }

    public LiveData<String> getFavoriteMovie() {
        return favoriteMovie;
    }

    public void setText(Movie movie) {
        if (movie == null || movie.getName() == null || movie.getName().trim().isEmpty()) {
            favoriteMovie.setValue("Введите название фильма");
            return;
        }
        boolean result = saveMovieToFavorite.execute(movie);
        favoriteMovie.setValue(result
                ? "Фильм сохранён: " + movie.getName().trim()
                : "Не удалось сохранить фильм");
    }

    public void getText() {
        Movie movie = getFavoriteFilm.execute();
        favoriteMovie.setValue(movie == null
                ? "Любимый фильм ещё не сохранён"
                : "Любимый фильм: " + movie.getName());
    }

    @Override
    protected void onCleared() {
        Log.d("MainViewModel", "MainViewModel cleared");
        super.onCleared();
    }
}

package ru.mirea.ptitsyn.lesson9.domain;

import ru.mirea.ptitsyn.lesson9.domain.models.Movie;
import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;

public final class SaveMovieToFavoriteUseCase {
    private final MovieRepository movieRepository;

    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public boolean execute(Movie movie) {
        if (movie == null || movie.getName() == null
                || movie.getName().trim().isEmpty()) {
            return false;
        }
        return movieRepository.saveMovie(
                new Movie(movie.getId(), movie.getName().trim())
        );
    }
}

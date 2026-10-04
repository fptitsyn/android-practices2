package ru.mirea.ptitsyn.lesson9.domain;

import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;
import ru.mirea.ptitsyn.lesson9.domain.models.Movie;

public class SaveMovieToFavoriteUseCase {
    private MovieRepository movieRepository;
    public SaveMovieToFavoriteUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public boolean execute(Movie movie){
        return movieRepository.saveMovie(movie);
    }
}
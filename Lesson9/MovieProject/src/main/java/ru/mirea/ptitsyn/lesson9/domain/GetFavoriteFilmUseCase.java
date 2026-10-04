package ru.mirea.ptitsyn.lesson9.domain;

import ru.mirea.ptitsyn.lesson9.domain.models.Movie;
import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;

public class GetFavoriteFilmUseCase {
    private MovieRepository movieRepository;
    public GetFavoriteFilmUseCase(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
    public Movie execute(){
        return movieRepository.getMovie();
    }
}
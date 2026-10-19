package ru.mirea.ptitsyn.lesson9.data.repository;

import java.time.LocalDate;

import ru.mirea.ptitsyn.lesson9.data.storage.MovieStorage;
import ru.mirea.ptitsyn.lesson9.data.storage.models.MovieModel;
import ru.mirea.ptitsyn.lesson9.domain.models.Movie;
import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;

public final class MovieRepositoryImpl implements MovieRepository {
    private final MovieStorage movieStorage;

    public MovieRepositoryImpl(MovieStorage movieStorage) {
        this.movieStorage = movieStorage;
    }

    @Override
    public boolean saveMovie(Movie movie) {
        return movieStorage.save(mapToStorage(movie));
    }

    @Override
    public Movie getMovie() {
        MovieModel movie = movieStorage.get();
        return movie == null ? null : mapToDomain(movie);
    }

    private MovieModel mapToStorage(Movie movie) {
        return new MovieModel(
                movie.getId(), movie.getName(), LocalDate.now().toString()
        );
    }

    private Movie mapToDomain(MovieModel movieModel) {
        return new Movie(movieModel.getId(), movieModel.getName());
    }
}

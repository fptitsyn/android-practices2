package ru.mirea.ptitsyn.lesson9;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ru.mirea.ptitsyn.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.ptitsyn.lesson9.domain.GetFavoriteFilmUseCase;
import ru.mirea.ptitsyn.lesson9.domain.SaveMovieToFavoriteUseCase;
import ru.mirea.ptitsyn.lesson9.domain.models.Movie;
import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;

public class MainActivity extends AppCompatActivity {
    private EditText editTextMovie;
    private TextView textViewMovie;
    private SaveMovieToFavoriteUseCase saveMovieUseCase;
    private GetFavoriteFilmUseCase getFavoriteFilmUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextMovie = findViewById(R.id.editTextMovie);
        textViewMovie = findViewById(R.id.textViewMovie);

        MovieRepository movieRepository = new MovieRepositoryImpl(getApplicationContext());
        saveMovieUseCase = new SaveMovieToFavoriteUseCase(movieRepository);
        getFavoriteFilmUseCase = new GetFavoriteFilmUseCase(movieRepository);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(view -> {
            saveFavoriteMovie();
        });

        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> {
            showFavoriteMovie();
        });
    }

    private void saveFavoriteMovie() {
        String movieName = editTextMovie.getText().toString().trim();

        if (movieName.isEmpty()) {
            editTextMovie.setError("Введите название фильма");
            return;
        }

        Movie movie = new Movie(1, movieName);
        boolean result = saveMovieUseCase.execute(movie);

        if (result) {
            textViewMovie.setText("Фильм сохранён: " + movieName);
            editTextMovie.setText("");
        } else {
            textViewMovie.setText("Не удалось сохранить фильм");
        }
    }

    private void showFavoriteMovie() {
        Movie movie = getFavoriteFilmUseCase.execute();

        if (movie == null) {
            textViewMovie.setText("Любимый фильм ещё не сохранён");
            return;
        }

        textViewMovie.setText("Любимый фильм: " + movie.getName());
    }
}
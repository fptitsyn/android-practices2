package ru.mirea.ptitsyn.lesson9;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ptitsyn.lesson9.domain.models.Movie;
import ru.mirea.ptitsyn.lesson9.presentation.MainViewModel;
import ru.mirea.ptitsyn.lesson9.presentation.ViewModelFactory;

public final class MainActivity extends AppCompatActivity {
    private MainViewModel vm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MainActivity", "MainActivity created");

        EditText editTextMovie = findViewById(R.id.editTextMovie);
        TextView textViewMovie = findViewById(R.id.textViewMovie);

        vm = new ViewModelProvider(this, new ViewModelFactory(this))
                .get(MainViewModel.class);

        vm.getFavoriteMovie().observe(this, textViewMovie::setText);

        findViewById(R.id.buttonSaveMovie).setOnClickListener(view ->
                vm.setText(new Movie(1, editTextMovie.getText().toString())));

        findViewById(R.id.buttonGetMovie).setOnClickListener(view -> vm.getText());
    }
}

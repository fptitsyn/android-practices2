package ru.mirea.ptitsyn.lesson9.presentation;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ptitsyn.lesson9.data.repository.MovieRepositoryImpl;
import ru.mirea.ptitsyn.lesson9.data.storage.MovieStorage;
import ru.mirea.ptitsyn.lesson9.data.storage.sharedprefs.SharedPrefMovieStorage;
import ru.mirea.ptitsyn.lesson9.domain.repository.MovieRepository;

public final class ViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.equals(MainViewModel.class)) {
            throw new IllegalArgumentException("Unknown ViewModel: " + modelClass.getName());
        }
        MovieStorage movieStorage = new SharedPrefMovieStorage(context);
        MovieRepository movieRepository = new MovieRepositoryImpl(movieStorage);
        return modelClass.cast(new MainViewModel(movieRepository));
    }
}

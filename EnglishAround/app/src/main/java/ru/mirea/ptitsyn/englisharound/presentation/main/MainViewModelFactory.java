package ru.mirea.ptitsyn.englisharound.presentation.main;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ptitsyn.data.database.AppDatabase;
import ru.mirea.ptitsyn.data.network.NetworkApi;
import ru.mirea.ptitsyn.data.repository.AuthRepositoryImpl;
import ru.mirea.ptitsyn.data.repository.UserDictionaryRepositoryImpl;
import ru.mirea.ptitsyn.data.repository.UserRepositoryImpl;
import ru.mirea.ptitsyn.data.repository.WordRepositoryImpl;
import ru.mirea.ptitsyn.data.storage.UserPreferences;

import ru.mirea.ptitsyn.domain.repository.AuthRepository;
import ru.mirea.ptitsyn.domain.repository.UserDictionaryRepository;
import ru.mirea.ptitsyn.domain.repository.UserRepository;
import ru.mirea.ptitsyn.domain.repository.WordRepository;

public final class MainViewModelFactory implements ViewModelProvider.Factory {

    private final Context applicationContext;

    public MainViewModelFactory(Context context) {
        applicationContext = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.equals(MainViewModel.class)) {
            throw new IllegalArgumentException("Неизвестный класс ViewModel: " + modelClass.getName());
        }

        AuthRepository authRepository = new AuthRepositoryImpl();
        UserRepository userRepository = new UserRepositoryImpl(new UserPreferences(applicationContext));
        WordRepository wordRepository = new WordRepositoryImpl(new NetworkApi());

        UserDictionaryRepository dictionaryRepository =
                new UserDictionaryRepositoryImpl(AppDatabase.getInstance(applicationContext).dictionaryWordDao());

        MainViewModel viewModel = new MainViewModel(
                authRepository,
                userRepository,
                wordRepository,
                dictionaryRepository,
                ContextCompat.getMainExecutor(applicationContext)
        );

        return modelClass.cast(viewModel);
    }
}
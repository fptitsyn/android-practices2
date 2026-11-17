package ru.mirea.ptitsyn.englisharound.presentation.recognition;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ptitsyn.data.repository.RecognitionRepositoryImpl;

public final class RecognitionViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public RecognitionViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (!modelClass.equals(RecognitionViewModel.class)) {
            throw new IllegalArgumentException("Неизвестная ViewModel: " + modelClass.getName());
        }

        return modelClass.cast(new RecognitionViewModel(new RecognitionRepositoryImpl(context)));
    }
}
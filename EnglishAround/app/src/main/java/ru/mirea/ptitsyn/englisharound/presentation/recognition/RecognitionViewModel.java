package ru.mirea.ptitsyn.englisharound.presentation.recognition;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.mirea.ptitsyn.domain.models.RecognitionResult;
import ru.mirea.ptitsyn.domain.repository.RecognitionRepository;
import ru.mirea.ptitsyn.domain.usecases.recognition.RecognizeObjectsUseCase;

public final class RecognitionViewModel extends ViewModel {
    private final RecognitionRepository repository;
    private final RecognizeObjectsUseCase recognizeObjects;
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    private final MutableLiveData<String> imageUri = new MutableLiveData<>("");
    private final MutableLiveData<List<RecognitionResult>> results =
            new MutableLiveData<>(Collections.emptyList());
    private final MutableLiveData<RecognitionResult> selected = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>(false);
    private final MutableLiveData<String> error = new MutableLiveData<>("");

    private volatile boolean cleared;

    public RecognitionViewModel(RecognitionRepository repository) {
        this.repository = repository;
        recognizeObjects = new RecognizeObjectsUseCase(repository);
    }

    public LiveData<String> getImageUri() { return imageUri; }
    public LiveData<List<RecognitionResult>> getResults() { return results; }
    public LiveData<RecognitionResult> getSelected() { return selected; }
    public LiveData<Boolean> getLoading() { return loading; }
    public LiveData<String> getError() { return error; }

    public void select(RecognitionResult result) {
        selected.setValue(result);
    }

    public void recognize(String uri) {
        if (cleared || Boolean.TRUE.equals(loading.getValue())) {
            return;
        }

        imageUri.setValue(uri);
        results.setValue(Collections.emptyList());
        selected.setValue(null);
        error.setValue("");
        loading.setValue(true);

        worker.execute(() -> {
            try {
                List<RecognitionResult> candidates = recognizeObjects.execute(uri);

                if (!cleared) {
                    results.postValue(candidates);
                }
            } catch (Exception exception) {
                if (!cleared) {
                    error.postValue(exception.getMessage() == null ? "Ошибка распознавания" : exception.getMessage());
                }
            } finally {
                if (!cleared) {
                    loading.postValue(false);
                }
            }
        });
    }

    @Override
    protected void onCleared() {
        cleared = true;
        worker.execute(repository::close);
        worker.shutdown();
        super.onCleared();
    }
}
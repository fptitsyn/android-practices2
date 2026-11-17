package ru.mirea.ptitsyn.domain.usecases.recognition;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.RecognitionResult;
import ru.mirea.ptitsyn.domain.repository.RecognitionRepository;

public final class RecognizeObjectsUseCase {
    private final RecognitionRepository repository;

    public RecognizeObjectsUseCase(RecognitionRepository repository) {
        this.repository = repository;
    }

    public List<RecognitionResult> execute(String imageUri) {
        return repository.recognize(imageUri);
    }
}
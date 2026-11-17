package ru.mirea.ptitsyn.domain.repository;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.RecognitionResult;

public interface RecognitionRepository extends AutoCloseable {
    List<RecognitionResult> recognize(String imageUri);

    @Override
    void close();
}
package ru.mirea.ptitsyn.data.repository;

import ru.mirea.ptitsyn.domain.repository.StudySessionRepository;
import java.util.Arrays;
import java.util.List;

public class StudySessionRepositoryImpl implements StudySessionRepository {
    @Override
    public List<String> execute() {
        return Arrays.asList("book", "cup", "apple");
    }
}

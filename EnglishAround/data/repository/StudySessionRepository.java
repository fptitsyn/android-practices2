package ru.mirea.ptitsyn.englisharound.data.repository;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ptitsyn.englisharound.domain.repository.IStudySessionRepository;

public class StudySessionRepository implements IStudySessionRepository {
    private ArrayList<String> studySessions = new ArrayList<>();
    @Override
    public List<String> execute() {
        return studySessions;
    }
}

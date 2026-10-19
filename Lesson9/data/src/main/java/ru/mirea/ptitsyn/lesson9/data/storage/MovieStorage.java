package ru.mirea.ptitsyn.lesson9.data.storage;

import ru.mirea.ptitsyn.lesson9.data.storage.models.MovieModel;

public interface MovieStorage {
    MovieModel get();
    boolean save(MovieModel movieModel);
}

package ru.mirea.ptitsyn.domain.repository;

import ru.mirea.ptitsyn.domain.models.User;

public interface UserRepository {
    void saveUser(User user);

    User getUser();

    void clearUser();
}
package ru.mirea.ptitsyn.data.repository;

import ru.mirea.ptitsyn.data.storage.UserPreferences;
import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.UserRepository;

public final class UserRepositoryImpl implements UserRepository {
    private final UserPreferences preferences;

    public UserRepositoryImpl(UserPreferences preferences) {
        this.preferences = preferences;
    }

    @Override
    public void saveUser(User user) {
        preferences.save(user);
    }

    @Override
    public User getUser() {
        return preferences.get();
    }

    @Override
    public void clearUser() {
        preferences.clear();
    }
}
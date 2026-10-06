package ru.mirea.ptitsyn.domain.repository;

import ru.mirea.ptitsyn.domain.models.User;

public interface AuthCallback {
    void onSuccess(User user);
    void onError(String message);
}

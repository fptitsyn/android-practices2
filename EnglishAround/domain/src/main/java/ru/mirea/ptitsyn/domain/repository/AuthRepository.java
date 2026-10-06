package ru.mirea.ptitsyn.domain.repository;

import ru.mirea.ptitsyn.domain.models.User;

public interface AuthRepository {
    void login(String email, String password, AuthCallback callback);
    void register(String name, String email, String password, AuthCallback callback);
    void resetPassword(String email, AuthCallback callback);
    User getCurrentUser();
    void logout();
}

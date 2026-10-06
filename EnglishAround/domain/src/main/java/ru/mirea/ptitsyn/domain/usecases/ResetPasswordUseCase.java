package ru.mirea.ptitsyn.domain.usecases;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.AuthCallback;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;

public final class ResetPasswordUseCase {
    private final AuthRepository repository;
    public ResetPasswordUseCase(AuthRepository repository) { this.repository = repository; }
    public void execute(String email, AuthCallback callback) { repository.resetPassword(email, callback); }
}

package ru.mirea.ptitsyn.domain.usecases;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.AuthCallback;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;

public final class LogoutUserUseCase {
    private final AuthRepository repository;
    public LogoutUserUseCase(AuthRepository repository) { this.repository = repository; }
    public void execute() { repository.logout(); }
}

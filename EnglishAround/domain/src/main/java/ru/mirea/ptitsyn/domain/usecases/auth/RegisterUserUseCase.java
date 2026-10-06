package ru.mirea.ptitsyn.domain.usecases.auth;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.AuthCallback;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;

public final class RegisterUserUseCase {
    private final AuthRepository repository;
    public RegisterUserUseCase(AuthRepository repository) { this.repository = repository; }
    public void execute(String name, String email, String password, AuthCallback callback) { repository.register(name, email, password, callback); }
}

package ru.mirea.ptitsyn.domain.usecases;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.AuthCallback;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;

public final class LoginUserUseCase {
    private final AuthRepository repository;
    public LoginUserUseCase(AuthRepository repository) { this.repository = repository; }
    public void execute(String email, String password, AuthCallback callback) { repository.login(email, password, callback); }
}

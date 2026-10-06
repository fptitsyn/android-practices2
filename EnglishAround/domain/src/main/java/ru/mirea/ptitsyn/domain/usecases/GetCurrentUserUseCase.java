package ru.mirea.ptitsyn.domain.usecases;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;

public final class GetCurrentUserUseCase {
    private final AuthRepository repository;
    public GetCurrentUserUseCase(AuthRepository repository) { this.repository = repository; }
    public User execute() { return repository.getCurrentUser(); }
}

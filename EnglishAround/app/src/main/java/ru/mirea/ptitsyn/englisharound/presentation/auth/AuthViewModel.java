package ru.mirea.ptitsyn.englisharound.presentation.auth;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ru.mirea.ptitsyn.englisharound.data.repository.AuthRepositoryImpl;
import ru.mirea.ptitsyn.englisharound.domain.models.User;
import ru.mirea.ptitsyn.englisharound.domain.repository.AuthCallback;
import ru.mirea.ptitsyn.englisharound.domain.repository.AuthRepository;
import ru.mirea.ptitsyn.englisharound.domain.usecases.*;

public final class AuthViewModel extends ViewModel {
    // Dependency assembly. Firebase types are confined to data.
    private final AuthRepository repository = new AuthRepositoryImpl();
    private final LoginUserUseCase login = new LoginUserUseCase(repository);
    private final RegisterUserUseCase register = new RegisterUserUseCase(repository);
    private final ResetPasswordUseCase reset = new ResetPasswordUseCase(repository);
    private final MutableLiveData<AuthState> state = new MutableLiveData<>(
            new AuthState(AuthState.Status.IDLE, null, ""));

    public LiveData<AuthState> getState() { return state; }
    public User getCurrentUser() { return new GetCurrentUserUseCase(repository).execute(); }
    public boolean isLoading() {
        return state.getValue() != null && state.getValue().status == AuthState.Status.LOADING;
    }
    public void clearMessage() {
        state.setValue(new AuthState(AuthState.Status.IDLE, null, ""));
    }
    private void loading() {
        state.setValue(new AuthState(AuthState.Status.LOADING, null, ""));
    }
    public void login(String email, String password) {
        if (isLoading()) return;
        loading();
        login.execute(email, password, callback(false, false));
    }
    public void register(String name, String email, String password) {
        if (isLoading()) return;
        loading();
        register.execute(name, email, password, callback(true, false));
    }
    public void resetPassword(String email) {
        if (isLoading()) return;
        loading();
        reset.execute(email, callback(false, true));
    }
    private AuthCallback callback(boolean registration, boolean resetRequest) {
        return new AuthCallback() {
            @Override public void onSuccess(User user) {
                String message = "";
                if (resetRequest) {
                    message = "Если адрес зарегистрирован, на него отправлена ссылка для сброса пароля.";
                } else if (registration && user != null && user.getName().isEmpty()) {
                    message = "Аккаунт создан, но имя не удалось сохранить.";
                }
                state.setValue(new AuthState(
                        resetRequest ? AuthState.Status.RESET_SENT : AuthState.Status.SUCCESS,
                        user, message));
            }
            @Override public void onError(String message) {
                state.setValue(new AuthState(AuthState.Status.ERROR, null, message));
            }
        };
    }
}

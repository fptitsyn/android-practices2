package ru.mirea.ptitsyn.data.repository;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.AuthCallback;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;

public final class AuthRepositoryImpl implements AuthRepository {
    private final FirebaseAuth auth;

    public AuthRepositoryImpl() { auth = FirebaseAuth.getInstance(); }

    @Override
    public void login(String email, String password, AuthCallback callback) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && auth.getCurrentUser() != null) {
                        callback.onSuccess(map(auth.getCurrentUser()));
                    } else {
                        callback.onError(error(task.getException()));
                    }
                });
    }

    @Override
    public void register(String name, String email, String password, AuthCallback callback) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || auth.getCurrentUser() == null) {
                        callback.onError(error(task.getException()));
                        return;
                    }
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();
                    firebaseUser.updateProfile(profile).addOnCompleteListener(profileTask -> {
                        // The account exists even if updating its display name failed.
                        callback.onSuccess(map(firebaseUser));
                    });
                });
    }

    @Override
    public void resetPassword(String email, AuthCallback callback) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) callback.onSuccess(null);
            else callback.onError(error(task.getException()));
        });
    }

    @Override
    public User getCurrentUser() {
        return auth.getCurrentUser() == null ? null : map(auth.getCurrentUser());
    }

    @Override
    public void logout() { auth.signOut(); }

    private static User map(FirebaseUser user) {
        return new User(user.getUid(), user.getDisplayName(), user.getEmail());
    }

    private static String error(Exception exception) {
        if (exception instanceof FirebaseNetworkException) {
            return "Нет подключения к интернету. Попробуйте ещё раз.";
        }
        if (exception instanceof FirebaseTooManyRequestsException) {
            return "Слишком много попыток. Попробуйте позже.";
        }
        if (exception instanceof FirebaseAuthException) {
            String code = ((FirebaseAuthException) exception).getErrorCode();
            switch (code) {
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    return "Этот адрес уже зарегистрирован. Перейдите ко входу.";
                case "ERROR_WEAK_PASSWORD":
                    return "Пароль не соответствует требованиям Firebase.";
                case "ERROR_INVALID_EMAIL":
                    return "Проверьте адрес электронной почты.";
                case "ERROR_USER_DISABLED":
                    return "Учётная запись отключена.";
                case "ERROR_OPERATION_NOT_ALLOWED":
                    return "Включите Email/Password в Firebase Console.";
                case "ERROR_WRONG_PASSWORD":
                case "ERROR_USER_NOT_FOUND":
                case "ERROR_INVALID_CREDENTIAL":
                case "ERROR_INVALID_LOGIN_CREDENTIALS":
                    return "Неверная электронная почта или пароль.";
            }
        }
        return "Не удалось выполнить запрос. Попробуйте ещё раз.";
    }
}

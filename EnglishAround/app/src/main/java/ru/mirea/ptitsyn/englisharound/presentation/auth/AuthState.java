package ru.mirea.ptitsyn.englisharound.presentation.auth;

import com.google.firebase.firestore.auth.User;
import ru.mirea.ptitsyn.domain.models.User;

public final class AuthState {
    public enum Status { IDLE, LOADING, ERROR, SUCCESS, RESET_SENT }
    public final Status status;
    public final User user;
    public final String message;

    public AuthState(Status status, User user, String message) {
        this.status = status;
        this.user = user;
        this.message = message;
    }
}

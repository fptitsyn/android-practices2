package ru.mirea.ptitsyn.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

import ru.mirea.ptitsyn.domain.models.User;

public final class UserPreferences {
    private static final String FILE_NAME = "user_preferences";
    private static final String KEY_ID = "user_id";
    private static final String KEY_NAME = "user_name";
    private static final String KEY_EMAIL = "user_email";

    private final SharedPreferences preferences;

    public UserPreferences(Context context) {
        preferences = context.getApplicationContext()
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
    }

    public void save(User user) {
        preferences.edit()
                .putString(KEY_ID, user.getId())
                .putString(KEY_NAME, user.getName())
                .putString(KEY_EMAIL, user.getEmail())
                .apply();
    }

    public User get() {
        String id = preferences.getString(KEY_ID, null);

        if (id == null) {
            return null;
        }

        return new User(
                id,
                preferences.getString(KEY_NAME, ""),
                preferences.getString(KEY_EMAIL, "")
        );
    }

    public void clear() {
        preferences.edit().clear().apply();
    }
}
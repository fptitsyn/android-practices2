package ru.mirea.ptitsyn.englisharound.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.data.repository.AuthRepositoryImpl;
import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.repository.AuthRepository;
import ru.mirea.ptitsyn.domain.usecases.auth.GetCurrentUserUseCase;
import ru.mirea.ptitsyn.domain.usecases.auth.LogoutUserUseCase;
import ru.mirea.ptitsyn.englisharound.presentation.auth.AuthUi;

public final class MainActivity extends AppCompatActivity {
    private AuthRepository repository;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AuthUi.applyInsets(this, findViewById(R.id.mainRoot));
        repository = new AuthRepositoryImpl();
        findViewById(R.id.logoutButton).setOnClickListener(v -> {
            new LogoutUserUseCase(repository).execute();
            openLogin();
        });
    }

    @Override protected void onStart() {
        super.onStart();
        User user = new GetCurrentUserUseCase(repository).execute();
        if (user == null) {
            openLogin();
            return;
        }
        String name = user.getName().isEmpty() ? user.getEmail() : user.getName();
        ((TextView) findViewById(R.id.welcomeText)).setText("Здравствуйте, " + name + "!");
        ((TextView) findViewById(R.id.emailText)).setText(user.getEmail());
    }

    private void openLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}

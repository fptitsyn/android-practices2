package ru.mirea.ptitsyn.englisharound.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.auth.*;

public final class RegisterActivity extends AppCompatActivity {
    private AuthViewModel model;
    private TextInputLayout nameLayout, emailLayout, passwordLayout, confirmLayout;
    private TextInputEditText nameInput, emailInput, passwordInput, confirmInput;
    private MaterialButton registerButton;
    private View backButton, loginLink;
    private TextView errorText;
    private ProgressBar progress;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        AuthUi.applyInsets(this, findViewById(R.id.registerRoot));
        model = new ViewModelProvider(this).get(AuthViewModel.class);
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmLayout = findViewById(R.id.confirmLayout);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmInput = findViewById(R.id.confirmInput);
        registerButton = findViewById(R.id.registerButton);
        backButton = findViewById(R.id.backButton);
        loginLink = findViewById(R.id.loginLink);
        errorText = findViewById(R.id.errorText);
        progress = findViewById(R.id.progress);

        backButton.setOnClickListener(v -> finish());
        loginLink.setOnClickListener(v -> finish());
        registerButton.setOnClickListener(v -> submit());
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() {
                if (!model.isLoading()) finish();
            }
        });
        model.getState().observe(this, state -> {
            boolean busy = state.status == AuthState.Status.LOADING;
            registerButton.setEnabled(!busy);
            backButton.setEnabled(!busy);
            loginLink.setEnabled(!busy);
            nameInput.setEnabled(!busy);
            emailInput.setEnabled(!busy);
            passwordInput.setEnabled(!busy);
            confirmInput.setEnabled(!busy);
            progress.setVisibility(busy ? View.VISIBLE : View.GONE);
            errorText.setText(state.message);
            errorText.setVisibility(state.status == AuthState.Status.ERROR ? View.VISIBLE : View.GONE);
            if (state.status == AuthState.Status.SUCCESS && !isFinishing()) {
                if (!state.message.isEmpty()) {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show();
                }
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
    }

    private void submit() {
        nameLayout.setError(null);
        emailLayout.setError(null);
        passwordLayout.setError(null);
        confirmLayout.setError(null);
        String name = value(nameInput).trim();
        String email = value(emailInput).trim();
        String password = value(passwordInput);
        String confirm = value(confirmInput);
        boolean valid = true;
        if (name.isEmpty()) { nameLayout.setError("Введите имя"); valid = false; }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Введите корректную электронную почту"); valid = false;
        }
        if (password.length() < 6) {
            passwordLayout.setError("Минимум 6 символов"); valid = false;
        }
        if (!password.equals(confirm)) {
            confirmLayout.setError("Пароли не совпадают"); valid = false;
        }
        if (valid) model.register(name, email, password);
    }

    private static String value(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString();
    }
}

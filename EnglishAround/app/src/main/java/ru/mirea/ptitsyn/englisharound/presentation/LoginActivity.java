package ru.mirea.ptitsyn.englisharound.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.auth.*;

public final class LoginActivity extends AppCompatActivity {
    private AuthViewModel model;
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private MaterialButton loginButton;
    private TextView errorText, registerLink, resetLink;
    private ProgressBar progress;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AuthUi.applyInsets(this, findViewById(R.id.loginRoot));
        model = new ViewModelProvider(this).get(AuthViewModel.class);
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginButton = findViewById(R.id.loginButton);
        registerLink = findViewById(R.id.registerLink);
        resetLink = findViewById(R.id.resetLink);
        errorText = findViewById(R.id.errorText);
        progress = findViewById(R.id.progress);

        loginButton.setOnClickListener(v -> submit());
        registerLink.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
        resetLink.setOnClickListener(v -> resetPassword());
        model.getState().observe(this, state -> {
            boolean busy = state.status == AuthState.Status.LOADING;
            loginButton.setEnabled(!busy);
            registerLink.setEnabled(!busy);
            resetLink.setEnabled(!busy);
            emailInput.setEnabled(!busy);
            passwordInput.setEnabled(!busy);
            progress.setVisibility(busy ? View.VISIBLE : View.GONE);
            errorText.setText(state.message);
            errorText.setVisibility(state.status == AuthState.Status.ERROR ? View.VISIBLE : View.GONE);
            if (state.status == AuthState.Status.SUCCESS) openMain();
            if (state.status == AuthState.Status.RESET_SENT) {
                Toast.makeText(this, state.message, Toast.LENGTH_LONG).show();
                model.clearMessage();
            }
        });
    }

    @Override protected void onStart() {
        super.onStart();
        if (!model.isLoading() && model.getCurrentUser() != null) openMain();
    }

    private boolean validEmail() {
        emailLayout.setError(null);
        String email = value(emailInput).trim();
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Введите корректную электронную почту");
            return false;
        }
        return true;
    }

    private void submit() {
        passwordLayout.setError(null);
        boolean valid = validEmail();
        if (value(passwordInput).isEmpty()) {
            passwordLayout.setError("Введите пароль");
            valid = false;
        }
        if (valid) model.login(value(emailInput).trim(), value(passwordInput));
    }

    private void resetPassword() {
        if (validEmail()) model.resetPassword(value(emailInput).trim());
    }

    private void openMain() {
        if (isFinishing()) return;
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private static String value(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString();
    }
}

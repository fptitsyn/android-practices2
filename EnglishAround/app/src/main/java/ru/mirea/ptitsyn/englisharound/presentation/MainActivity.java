package ru.mirea.ptitsyn.englisharound.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;

import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.auth.AuthUi;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainUiState;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;

public final class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private MainViewModel model;
    private MainUiState currentState;

    private TextView welcomeText;
    private TextView emailText;
    private TextView errorText;
    private TextView dictionaryTitle;

    private ProgressBar progress;
    private Button refreshButton;
    private Button logoutButton;

    private ListView catalogList;
    private ListView dictionaryList;

    private ArrayAdapter<String> catalogAdapter;
    private ArrayAdapter<String> dictionaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "MainActivity created");

        AuthUi.applyInsets(this, findViewById(R.id.mainRoot));

        bindViews();
        configureLists();

        model = new ViewModelProvider(this, new MainViewModelFactory(this)).get(MainViewModel.class);

        configureActions();

        model.getState().observe(this, this::render);
    }

    private void bindViews() {
        welcomeText = findViewById(R.id.welcomeText);
        emailText = findViewById(R.id.emailText);
        errorText = findViewById(R.id.errorText);
        dictionaryTitle = findViewById(R.id.dictionaryTitle);

        progress = findViewById(R.id.progress);
        refreshButton = findViewById(R.id.refreshButton);
        logoutButton = findViewById(R.id.logoutButton);

        catalogList = findViewById(R.id.catalogList);
        dictionaryList = findViewById(R.id.dictionaryList);

        dictionaryList.setEmptyView(findViewById(R.id.dictionaryEmptyText));
    }

    private void configureLists() {
        catalogAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        dictionaryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());

        catalogList.setAdapter(catalogAdapter);
        dictionaryList.setAdapter(dictionaryAdapter);
    }

    private void configureActions() {
        refreshButton.setOnClickListener(view -> model.refresh());
        logoutButton.setOnClickListener(view -> model.logout());

        catalogList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    if (currentState == null
                            || currentState.isLoading()
                            || position >= currentState.getCatalog().size()) {
                        return;
                    }

                    model.addToDictionary(currentState.getCatalog().get(position));
                }
        );

        dictionaryList.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    if (currentState == null
                            || currentState.isLoading()
                            || position >= currentState.getDictionary().size()) {
                        return true;
                    }

                    model.removeFromDictionary(currentState.getDictionary().get(position));

                    return true;
                }
        );
    }

    private void render(MainUiState state) {
        currentState = state;

        if (state.isSignedOut()) {
            openLogin();
            return;
        }

        boolean loading = state.isLoading();

        progress.setVisibility(loading ? View.VISIBLE : View.GONE);

        refreshButton.setEnabled(!loading);
        logoutButton.setEnabled(!loading);
        catalogList.setEnabled(!loading);
        dictionaryList.setEnabled(!loading);

        errorText.setText(state.getError());
        errorText.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);

        if (state.getUser() != null) {
            String name = state.getUser().getName().isEmpty()
                    ? state.getUser().getEmail()
                    : state.getUser().getName();

            welcomeText.setText("Здравствуйте, " + name + "!");
            emailText.setText(state.getUser().getEmail());
        }

        dictionaryTitle.setText("Мой словарь: " + state.getDictionary().size());

        catalogAdapter.clear();
        catalogAdapter.addAll(state.getCatalogRows());
        catalogAdapter.notifyDataSetChanged();

        dictionaryAdapter.clear();
        dictionaryAdapter.addAll(state.getDictionaryRows());
        dictionaryAdapter.notifyDataSetChanged();
    }

    private void openLogin() {
        if (isFinishing()) {
            return;
        }

        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "MainActivity destroyed; changingConfigurations=" + isChangingConfigurations());

        super.onDestroy();
    }
}
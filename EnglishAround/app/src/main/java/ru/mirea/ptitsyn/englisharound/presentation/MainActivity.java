package ru.mirea.ptitsyn.englisharound.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.auth.AuthUi;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainUiState;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;

public final class MainActivity extends AppCompatActivity {

    private MainViewModel model;

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

    private final List<Word> catalogWords = new ArrayList<>();
    private final List<Word> dictionaryWords = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AuthUi.applyInsets(
                this,
                findViewById(R.id.mainRoot)
        );

        bindViews();
        configureLists();

        model = new ViewModelProvider(this)
                .get(MainViewModel.class);

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

        dictionaryList.setEmptyView(
                findViewById(R.id.dictionaryEmptyText)
        );
    }

    private void configureLists() {
        catalogAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );

        dictionaryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                new ArrayList<String>()
        );

        catalogList.setAdapter(catalogAdapter);
        dictionaryList.setAdapter(dictionaryAdapter);
    }

    private void configureActions() {
        refreshButton.setOnClickListener(
                view -> model.refresh()
        );

        logoutButton.setOnClickListener(
                view -> model.logout()
        );

        catalogList.setOnItemClickListener(
                (parent, view, position, id) -> {
                    if (position < catalogWords.size()) {
                        model.addToDictionary(
                                catalogWords.get(position)
                        );
                    }
                }
        );

        dictionaryList.setOnItemLongClickListener(
                (parent, view, position, id) -> {
                    if (position < dictionaryWords.size()) {
                        model.removeFromDictionary(
                                dictionaryWords.get(position)
                        );
                    }

                    return true;
                }
        );
    }

    private void render(MainUiState state) {
        if (state.isSignedOut()) {
            openLogin();
            return;
        }

        boolean loading = state.isLoading();

        progress.setVisibility(
                loading ? View.VISIBLE : View.GONE
        );

        refreshButton.setEnabled(!loading);
        logoutButton.setEnabled(!loading);
        catalogList.setEnabled(!loading);
        dictionaryList.setEnabled(!loading);

        errorText.setText(state.getError());
        errorText.setVisibility(
                state.getError().isEmpty()
                        ? View.GONE
                        : View.VISIBLE
        );

        User user = state.getUser();

        if (user != null) {
            String name = user.getName().isEmpty()
                    ? user.getEmail()
                    : user.getName();

            welcomeText.setText("Здравствуйте, " + name + "!");
            emailText.setText(user.getEmail());
        }

        catalogWords.clear();
        catalogWords.addAll(state.getCatalog());

        dictionaryWords.clear();
        dictionaryWords.addAll(state.getDictionary());

        dictionaryTitle.setText(
                "Мой словарь: " + dictionaryWords.size()
        );

        updateAdapters();
    }

    private void updateAdapters() {
        Set<Integer> savedIds = new HashSet<>();

        for (Word word : dictionaryWords) {
            savedIds.add(word.getId());
        }

        List<String> catalogRows = new ArrayList<>();

        for (Word word : catalogWords) {
            String row = formatWord(word);

            if (savedIds.contains(word.getId())) {
                row += "  ✓";
            }

            catalogRows.add(row);
        }

        List<String> dictionaryRows = new ArrayList<>();

        for (Word word : dictionaryWords) {
            dictionaryRows.add(formatWord(word));
        }

        catalogAdapter.clear();
        catalogAdapter.addAll(catalogRows);
        catalogAdapter.notifyDataSetChanged();

        dictionaryAdapter.clear();
        dictionaryAdapter.addAll(dictionaryRows);
        dictionaryAdapter.notifyDataSetChanged();
    }

    private String formatWord(Word word) {
        return word.getEnglish() + " — " + word.getTranslation();
    }

    private void openLogin() {
        if (isFinishing()) {
            return;
        }

        Intent intent = new Intent(
                this,
                LoginActivity.class
        );

        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TASK
        );

        startActivity(intent);
        finish();
    }
}
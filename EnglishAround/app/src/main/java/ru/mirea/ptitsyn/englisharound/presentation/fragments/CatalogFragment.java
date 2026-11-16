package ru.mirea.ptitsyn.englisharound.presentation.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;
import ru.mirea.ptitsyn.englisharound.presentation.main.WordAdapter;

public final class CatalogFragment extends Fragment {

    private RecyclerView recyclerView;
    private boolean busy = true;

    public CatalogFragment() {
        super(R.layout.fragment_word_list);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel model = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireContext())).get(MainViewModel.class);

        ((TextView) view.findViewById(R.id.listTitle)).setText("Каталог");
        ((TextView) view.findViewById(R.id.listHint)).setText(
                "Нажмите для просмотра статьи. Удерживайте для добавления в словарь.");

        Button refreshButton = view.findViewById(R.id.refreshButton);
        ProgressBar progress = view.findViewById(R.id.progress);
        TextView errorText = view.findViewById(R.id.errorText);
        TextView emptyText = view.findViewById(R.id.emptyText);

        WordAdapter adapter = new WordAdapter(this::openWord, word -> {
            if (!busy) {
                model.addToDictionary(word);
            }
        });

        recyclerView = view.findViewById(R.id.wordList);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        refreshButton.setOnClickListener(button -> model.refresh());

        model.getState().observe(getViewLifecycleOwner(), state -> {
            busy = state.isLoading();
            adapter.setItems(state.getCatalog());

            refreshButton.setEnabled(!busy);
            progress.setVisibility(busy ? View.VISIBLE : View.GONE);

            errorText.setText(state.getError());
            errorText.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);

            boolean empty = !busy && state.getCatalog().isEmpty();
            emptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    private void openWord(Word word) {
        Bundle arguments = new Bundle();
        arguments.putString("english", word.getEnglish());
        arguments.putString("translation", word.getTranslation());
        arguments.putString("image_url", word.getImageUrl());

        NavHostFragment.findNavController(this).navigate(R.id.action_catalog_to_word_details, arguments);
    }

    @Override
    public void onDestroyView() {
        recyclerView.setAdapter(null);
        recyclerView = null;
        super.onDestroyView();
    }
}
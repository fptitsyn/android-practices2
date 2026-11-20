package ru.mirea.ptitsyn.englisharound.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.databinding.FragmentWordListBinding;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;
import ru.mirea.ptitsyn.englisharound.presentation.main.WordAdapter;

public final class CatalogFragment extends Fragment {

    private FragmentWordListBinding binding;
    private boolean busy = true;

    private String query = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWordListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel model = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireActivity())).get(MainViewModel.class);

        binding.listTitle.setText("Каталог");
        WordAdapter.OnWordClickListener saveWord = null;

        if (model.isGuestMode()) {
            binding.listHint.setText("Нажмите на слово, чтобы открыть карточку.");
        } else {
            binding.listHint.setText("Нажмите для просмотра статьи. Удерживайте для добавления в словарь.");
            saveWord = word -> {
                if (!busy) {
                    model.addToDictionary(word);
                }
            };
        }

        WordAdapter adapter = new WordAdapter(this::openWord, saveWord);

        binding.wordList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.wordList.setAdapter(adapter);

        if (savedInstanceState != null) {
            query = savedInstanceState.getString("catalog_query", "");
        }

        binding.catalogSearch.setVisibility(View.VISIBLE);
        binding.catalogSearch.setQuery(query, false);
        binding.catalogSearch.clearFocus();
        binding.catalogSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String value) {
                binding.catalogSearch.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String value) {
                query = value;
                renderCatalog(model, adapter);
                return true;
            }
        });

        binding.refreshButton.setOnClickListener(button -> model.refresh());

        model.getState().observe(getViewLifecycleOwner(), state -> {
            busy = state.isLoading();
            renderCatalog(model, adapter);

            binding.refreshButton.setEnabled(!busy);
            binding.progress.setVisibility(busy ? View.VISIBLE : View.GONE);

            binding.errorText.setText(state.getError());
            binding.errorText.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);
        });
    }

    private void openWord(Word word) {
        Bundle arguments = new Bundle();
        arguments.putString("english", word.getEnglish());
        arguments.putString("translation", word.getTranslation());
        arguments.putString("image_url", word.getImageUrl());

        NavHostFragment.findNavController(this).navigate(R.id.action_catalog_to_word_details, arguments);
    }

    private void renderCatalog(MainViewModel model, WordAdapter adapter) {
        List<Word> visibleWords = model.searchCatalog(query);
        adapter.setItems(visibleWords);
        binding.emptyText.setText(query.trim().isEmpty() ? "Каталог пуст" : "Ничего не найдено");
        binding.emptyText.setVisibility(!busy && visibleWords.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("catalog_query", query);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        binding.catalogSearch.setOnQueryTextListener(null);
        binding.wordList.setAdapter(null);
        super.onDestroyView();
        binding = null;
    }
}
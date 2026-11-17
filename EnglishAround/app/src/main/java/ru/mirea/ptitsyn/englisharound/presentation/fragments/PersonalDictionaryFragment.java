package ru.mirea.ptitsyn.englisharound.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.databinding.FragmentWordListBinding;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;
import ru.mirea.ptitsyn.englisharound.presentation.main.WordAdapter;

public final class PersonalDictionaryFragment extends Fragment {

    private FragmentWordListBinding binding;
    private boolean busy = true;

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
                new MainViewModelFactory(requireContext())).get(MainViewModel.class);

        binding.listHint.setText("Нажмите для просмотра статьи. Удерживайте для удаления из словаря.");
        binding.emptyText.setText("Вы пока не добавили слова в личный словарь");

        WordAdapter adapter = new WordAdapter(this::openWord, word -> {
            if (!busy) {
                model.removeFromDictionary(word);
            }
        });

        binding.wordList.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.wordList.setAdapter(adapter);
        binding.refreshButton.setOnClickListener(button -> model.refresh());

        model.getState().observe(getViewLifecycleOwner(), state -> {
            busy = state.isLoading();

            binding.listTitle.setText("Мой словарь: " + state.getDictionary().size());
            adapter.setItems(state.getDictionary());

            binding.refreshButton.setEnabled(!busy);
            binding.progress.setVisibility(busy ? View.VISIBLE : View.GONE);

            binding.errorText.setText(state.getError());
            binding.errorText.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);

            boolean empty = !busy && state.getDictionary().isEmpty();
            binding.emptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
        });
    }

    private void openWord(Word word) {
        Bundle arguments = new Bundle();
        arguments.putString("english", word.getEnglish());
        arguments.putString("translation", word.getTranslation());
        arguments.putString("image_url", word.getImageUrl());

        NavHostFragment.findNavController(this).navigate(R.id.action_dictionary_to_word_details, arguments);
    }

    @Override
    public void onDestroyView() {
        binding.wordList.setAdapter(null);
        super.onDestroyView();
        binding = null;
    }
}
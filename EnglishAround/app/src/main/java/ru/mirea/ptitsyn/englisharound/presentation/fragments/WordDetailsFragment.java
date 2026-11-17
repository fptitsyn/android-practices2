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

import com.squareup.picasso.Picasso;

import ru.mirea.ptitsyn.domain.models.WordDetails;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.databinding.FragmentWordDetailsBinding;
import ru.mirea.ptitsyn.englisharound.presentation.main.DictionaryState;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;

public final class WordDetailsFragment extends Fragment {

    private FragmentWordDetailsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWordDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel model = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireContext())).get(MainViewModel.class);

        Bundle arguments = requireArguments();
        String english = arguments.getString("english", "");
        String translation = arguments.getString("translation", "");
        String imageUrl = arguments.getString("image_url", "");

        binding.detailsWord.setText(english);
        binding.detailsTranslation.setText(translation);
        binding.detailsImage.setImageResource(R.drawable.word_placeholder);

        if (imageUrl.startsWith("https://")) {
            int size = Math.round(120 * getResources().getDisplayMetrics().density);

            Picasso.get().load(imageUrl).resize(size, size).centerCrop()
                    .placeholder(R.drawable.word_placeholder).error(R.drawable.word_image_error)
                    .into(binding.detailsImage);
        }

        binding.backButton.setOnClickListener(button -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        binding.retryButton.setOnClickListener(button -> model.loadWordDetails(english));

        DictionaryState current = model.getDictionaryState().getValue();

        if (current == null || !english.equalsIgnoreCase(current.getWord())) {
            model.loadWordDetails(english);
        }

        model.getDictionaryState().observe(getViewLifecycleOwner(), state -> {
            if (state == null || !english.equalsIgnoreCase(state.getWord())) {
                return;
            }

            binding.detailsProgress.setVisibility(state.isLoading() ? View.VISIBLE : View.GONE);

            boolean hasError = !state.getError().isEmpty();
            binding.detailsError.setText(state.getError());
            binding.detailsError.setVisibility(hasError ? View.VISIBLE : View.GONE);
            binding.retryButton.setVisibility(hasError ? View.VISIBLE : View.GONE);

            if (state.isLoading() || hasError) {
                binding.detailsContent.setText("");
                return;
            }

            WordDetails details = state.getDetails();

            if (details == null) {
                binding.detailsContent.setText("Словарная статья отсутствует");
                return;
            }

            binding.detailsContent.setText(formatDetails(details));
        });
    }

    private String formatDetails(WordDetails details) {
        StringBuilder text = new StringBuilder();

        if (!details.getPhonetic().isEmpty()) {
            text.append("Транскрипция: ").append(details.getPhonetic()).append("\n\n");
        }

        if (!details.getPartOfSpeech().isEmpty()) {
            text.append("Часть речи: ").append(details.getPartOfSpeech()).append("\n\n");
        }

        text.append("Определение:\n").append(details.getDefinition());

        if (!details.getExample().isEmpty()) {
            text.append("\n\nПример:\n").append(details.getExample());
        }

        return text.toString();
    }

    @Override
    public void onDestroyView() {
        Picasso.get().cancelRequest(binding.detailsImage);
        super.onDestroyView();
        binding = null;
    }
}
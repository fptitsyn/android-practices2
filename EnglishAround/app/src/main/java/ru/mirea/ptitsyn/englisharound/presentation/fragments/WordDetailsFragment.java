package ru.mirea.ptitsyn.englisharound.presentation.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.squareup.picasso.Picasso;

import ru.mirea.ptitsyn.domain.models.WordDetails;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.main.DictionaryState;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;

public final class WordDetailsFragment extends Fragment {

    private ImageView image;

    public WordDetailsFragment() {
        super(R.layout.fragment_word_details);
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

        ((TextView) view.findViewById(R.id.detailsWord)).setText(english);
        ((TextView) view.findViewById(R.id.detailsTranslation)).setText(translation);

        image = view.findViewById(R.id.detailsImage);
        image.setImageResource(R.drawable.word_placeholder);

        if (imageUrl.startsWith("https://")) {
            int size = Math.round(120 * getResources().getDisplayMetrics().density);

            Picasso.get().load(imageUrl).resize(size, size).centerCrop()
                    .placeholder(R.drawable.word_placeholder).error(R.drawable.word_image_error).into(image);
        }

        ProgressBar progress = view.findViewById(R.id.detailsProgress);
        TextView errorText = view.findViewById(R.id.detailsError);
        TextView content = view.findViewById(R.id.detailsContent);
        Button retryButton = view.findViewById(R.id.retryButton);

        view.findViewById(R.id.backButton).setOnClickListener(button -> {
            NavHostFragment.findNavController(this).navigateUp();
        });

        retryButton.setOnClickListener(button -> model.loadWordDetails(english));

        DictionaryState current = model.getDictionaryState().getValue();

        if (current == null || !english.equalsIgnoreCase(current.getWord())) {
            model.loadWordDetails(english);
        }

        model.getDictionaryState().observe(getViewLifecycleOwner(), state -> {
            if (state == null || !english.equalsIgnoreCase(state.getWord())) {
                return;
            }

            progress.setVisibility(state.isLoading() ? View.VISIBLE : View.GONE);

            boolean hasError = !state.getError().isEmpty();
            errorText.setText(state.getError());
            errorText.setVisibility(hasError ? View.VISIBLE : View.GONE);
            retryButton.setVisibility(hasError ? View.VISIBLE : View.GONE);

            if (state.isLoading() || hasError) {
                content.setText("");
                return;
            }

            WordDetails details = state.getDetails();

            if (details == null) {
                content.setText("Словарная статья отсутствует");
                return;
            }

            content.setText(formatDetails(details));
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
        Picasso.get().cancelRequest(image);
        image = null;
        super.onDestroyView();
    }
}
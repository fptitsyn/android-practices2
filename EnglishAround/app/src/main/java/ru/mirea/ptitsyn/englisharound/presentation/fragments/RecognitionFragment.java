package ru.mirea.ptitsyn.englisharound.presentation.fragments;

import android.content.ActivityNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;

import ru.mirea.ptitsyn.domain.models.RecognitionResult;
import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.englisharound.databinding.FragmentRecognitionBinding;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainUiState;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;
import ru.mirea.ptitsyn.englisharound.presentation.recognition.RecognitionViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.recognition.RecognitionViewModelFactory;

public final class RecognitionFragment extends Fragment {
    private FragmentRecognitionBinding binding;
    private RecognitionViewModel recognitionModel;
    private MainViewModel mainModel;

    private String pendingCameraUri;
    private boolean recognitionBusy;
    private boolean mainBusy;

    private final ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(), success -> {
                if (success && pendingCameraUri != null) {
                    recognize(pendingCameraUri);
                }
            });

    private final ActivityResultLauncher<PickVisualMediaRequest> pickPhoto = registerForActivityResult(
            new ActivityResultContracts.PickVisualMedia(), uri -> {
                if (uri != null) {
                    recognize(uri.toString());
                }
            });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recognitionModel = new ViewModelProvider(this,
                new RecognitionViewModelFactory(requireContext())).get(RecognitionViewModel.class);

        mainModel = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireContext())).get(MainViewModel.class);

        if (savedInstanceState != null) {
            pendingCameraUri = savedInstanceState.getString("pending_camera_uri");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentRecognitionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.cameraButton.setOnClickListener(button -> openCamera());

        binding.galleryButton.setOnClickListener(button -> {
            PickVisualMediaRequest request = new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build();
            pickPhoto.launch(request);
        });

        binding.addWordButton.setOnClickListener(button -> saveWord());

        recognitionModel.getImageUri().observe(getViewLifecycleOwner(), uri -> {
            if (uri == null || uri.isEmpty()) {
                binding.photoPreview.setImageDrawable(null);
                return;
            }

            Picasso.get().load(Uri.parse(uri)).fit().centerInside().into(binding.photoPreview);
        });

        recognitionModel.getResults().observe(getViewLifecycleOwner(), results -> {
            binding.candidatesGroup.setOnCheckedChangeListener(null);
            binding.candidatesGroup.removeAllViews();

            RecognitionResult selected = recognitionModel.getSelected().getValue();
            int checkedId = -1;

            for (RecognitionResult result : results) {
                RadioButton button = new RadioButton(requireContext());
                button.setId(View.generateViewId());
                button.setTag(result);

                String text = String.format(Locale.getDefault(), "%s — оценка %.1f%%",
                        result.getWord(), result.getScore() * 100);

                if (!result.getLabel().equalsIgnoreCase(result.getWord())) {
                    text += "\nМетка модели: " + result.getLabel();
                }

                button.setText(text);
                binding.candidatesGroup.addView(button);

                if (selected != null && selected.getWord().equals(result.getWord())) {
                    checkedId = button.getId();
                }
            }

            if (checkedId != -1) {
                binding.candidatesGroup.check(checkedId);
            }

            binding.candidatesGroup.setOnCheckedChangeListener((group, checked) -> {
                View checkedView = group.findViewById(checked);

                if (checkedView != null) {
                    recognitionModel.select((RecognitionResult) checkedView.getTag());
                }
            });
        });

        recognitionModel.getSelected().observe(getViewLifecycleOwner(), result -> {
            String word = result == null ? "" : result.getWord();
            binding.englishInput.setText(word);
            binding.translationInput.setText(findTranslation(word));
            updateButtons();
        });

        recognitionModel.getLoading().observe(getViewLifecycleOwner(), loading -> {
            recognitionBusy = Boolean.TRUE.equals(loading);
            binding.recognitionProgress.setVisibility(recognitionBusy ? View.VISIBLE : View.GONE);
            updateButtons();
        });

        recognitionModel.getError().observe(getViewLifecycleOwner(), error -> {
            binding.recognitionError.setText(error);
        });

        mainModel.getState().observe(getViewLifecycleOwner(), state -> {
            mainBusy = state.isLoading();

            if (!state.getError().isEmpty()) {
                binding.saveMessage.setText(state.getError());
            }

            updateButtons();
        });

        mainModel.getRecognitionSaveMessage().observe(getViewLifecycleOwner(), message -> {
            binding.saveMessage.setText(message);
        });
    }

    private void recognize(String uri) {
        mainModel.clearRecognitionSaveMessage();
        recognitionModel.recognize(uri);
    }

    private void openCamera() {
        try {
            File directory = new File(requireContext().getCacheDir(), "recognition");

            if (!directory.exists() && !directory.mkdirs()) {
                throw new IllegalStateException("Не удалось создать папку для фотографии");
            }

            File photo = File.createTempFile("photo_", ".jpg", directory);
            String authority = requireContext().getPackageName() + ".fileprovider";
            Uri uri = FileProvider.getUriForFile(requireContext(), authority, photo);

            pendingCameraUri = uri.toString();
            takePicture.launch(uri);

        } catch (ActivityNotFoundException exception) {
            Toast.makeText(requireContext(), "На устройстве нет приложения камеры", Toast.LENGTH_LONG).show();

        } catch (Exception exception) {
            Toast.makeText(requireContext(), "Не удалось открыть камеру", Toast.LENGTH_LONG).show();
        }
    }

    private void saveWord() {
        String english = binding.englishInput.getText().toString().trim();
        String translation = binding.translationInput.getText().toString().trim();

        binding.englishInput.setError(null);
        binding.translationInput.setError(null);

        if (english.isEmpty()) {
            binding.englishInput.setError("Введите английское слово");
            return;
        }

        if (translation.isEmpty()) {
            binding.translationInput.setError("Введите перевод");
            return;
        }

        mainModel.saveRecognizedWord(english, translation);
    }

    private String findTranslation(String english) {
        MainUiState state = mainModel.getState().getValue();

        if (state != null) {
            for (Word word : state.getCatalog()) {
                if (word.getEnglish().equalsIgnoreCase(english)) {
                    return word.getTranslation();
                }
            }
        }

        return "";
    }

    private void updateButtons() {
        if (binding == null) {
            return;
        }

        boolean busy = recognitionBusy || mainBusy;
        boolean selected = recognitionModel.getSelected().getValue() != null;

        binding.cameraButton.setEnabled(!busy);
        binding.galleryButton.setEnabled(!busy);
        binding.addWordButton.setEnabled(!busy && selected);
        binding.englishInput.setEnabled(!busy && selected);
        binding.translationInput.setEnabled(!busy && selected);

        for (int i = 0; i < binding.candidatesGroup.getChildCount(); i++) {
            binding.candidatesGroup.getChildAt(i).setEnabled(!busy);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("pending_camera_uri", pendingCameraUri);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroyView() {
        Picasso.get().cancelRequest(binding.photoPreview);
        binding.candidatesGroup.setOnCheckedChangeListener(null);
        super.onDestroyView();
        binding = null;
    }
}
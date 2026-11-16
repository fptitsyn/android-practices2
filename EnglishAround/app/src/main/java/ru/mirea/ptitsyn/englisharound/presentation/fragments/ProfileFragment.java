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

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.englisharound.R;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;

public final class ProfileFragment extends Fragment {

    public ProfileFragment() {
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel model = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireContext())).get(MainViewModel.class);

        TextView nameText = view.findViewById(R.id.profileName);
        TextView emailText = view.findViewById(R.id.profileEmail);
        TextView idText = view.findViewById(R.id.profileId);
        TextView wordsText = view.findViewById(R.id.profileWords);
        TextView errorText = view.findViewById(R.id.profileError);
        ProgressBar progress = view.findViewById(R.id.profileProgress);
        Button refreshButton = view.findViewById(R.id.profileRefreshButton);
        Button logoutButton = view.findViewById(R.id.logoutButton);

        refreshButton.setOnClickListener(button -> model.refresh());
        logoutButton.setOnClickListener(button -> model.logout());

        model.getState().observe(getViewLifecycleOwner(), state -> {
            boolean loading = state.isLoading();

            progress.setVisibility(loading ? View.VISIBLE : View.GONE);
            refreshButton.setEnabled(!loading);
            logoutButton.setEnabled(!loading);

            errorText.setText(state.getError());
            errorText.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);

            User user = state.getUser();

            if (user == null) {
                nameText.setText(loading ? "Загрузка профиля…" : "Профиль не загружен");
                emailText.setText("");
                idText.setText("");
                wordsText.setText("");
                return;
            }

            nameText.setText(user.getName().isEmpty() ? "Пользователь" : user.getName());
            emailText.setText(user.getEmail());
            idText.setText("ID пользователя: " + user.getId());
            wordsText.setText("Слов в личном словаре: " + state.getDictionary().size());
        });
    }
}
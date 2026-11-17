package ru.mirea.ptitsyn.englisharound.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import ru.mirea.ptitsyn.domain.models.User;
import ru.mirea.ptitsyn.englisharound.databinding.FragmentProfileBinding;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModel;
import ru.mirea.ptitsyn.englisharound.presentation.main.MainViewModelFactory;

public final class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        MainViewModel model = new ViewModelProvider(requireActivity(),
                new MainViewModelFactory(requireContext())).get(MainViewModel.class);

        binding.profileRefreshButton.setOnClickListener(button -> model.refresh());
        binding.logoutButton.setOnClickListener(button -> model.logout());

        model.getState().observe(getViewLifecycleOwner(), state -> {
            boolean loading = state.isLoading();

            binding.profileProgress.setVisibility(loading ? View.VISIBLE : View.GONE);
            binding.profileRefreshButton.setEnabled(!loading);
            binding.logoutButton.setEnabled(!loading);

            binding.profileError.setText(state.getError());
            binding.profileError.setVisibility(state.getError().isEmpty() ? View.GONE : View.VISIBLE);

            User user = state.getUser();

            if (user == null) {
                binding.profileName.setText(loading ? "Загрузка профиля…" : "Профиль не загружен");
                binding.profileEmail.setText("");
                binding.profileId.setText("");
                binding.profileWords.setText("");
                return;
            }

            binding.profileName.setText(user.getName().isEmpty() ? "Пользователь" : user.getName());
            binding.profileEmail.setText(user.getEmail());
            binding.profileId.setText("ID пользователя: " + user.getId());
            binding.profileWords.setText("Слов в личном словаре: " + state.getDictionary().size());
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
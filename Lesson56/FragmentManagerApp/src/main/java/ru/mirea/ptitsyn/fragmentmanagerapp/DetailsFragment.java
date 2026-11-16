package ru.mirea.ptitsyn.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.TextView;
import androidx.lifecycle.ViewModelProvider;

public class DetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShareViewModel model = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        TextView name = view.findViewById(R.id.countryName);
        TextView capital = view.findViewById(R.id.countryCapital);
        TextView description = view.findViewById(R.id.countryDescription);
        model.getSelectedItem().observe(getViewLifecycleOwner(), country -> {
            if (country == null) return;
            name.setText(country.getName());
            capital.setText(getString(R.string.capital, country.getCapital()));
            description.setText(country.getDescription());
        });
    }
}

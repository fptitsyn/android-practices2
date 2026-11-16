package ru.mirea.ptitsyn.fragmentmanagerapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.lifecycle.ViewModelProvider;
import java.util.ArrayList;
import java.util.List;

public class HeaderFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_header, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShareViewModel model = new ViewModelProvider(requireActivity()).get(ShareViewModel.class);
        List<String> names = new ArrayList<>();

        for (Country country : model.getCountries()) {
            names.add(country.getName());
        }

        ListView list = view.findViewById(R.id.countryList);
        list.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_single_choice, names));
        list.setOnItemClickListener((parent, item, position, id) -> {
            model.selectItem(model.getCountries().get(position));
        });

        model.getSelectedItem().observe(getViewLifecycleOwner(), country -> {
            if (country != null) list.setItemChecked(model.getCountries().indexOf(country), true);
        });
    }
}

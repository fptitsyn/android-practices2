package ru.mirea.ptitsyn.fragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class BlankFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int numberStudent = requireArguments().getInt("studentNumber");
        Log.d("BlankFragment", "studentNumber=" + numberStudent);
        ((TextView) view.findViewById(R.id.numberText)).setText(getString(R.string.student_number, numberStudent));

        ListView todoList = view.findViewById(R.id.todoList);
        String[] todos = getResources().getStringArray(R.array.todos);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_list_item_multiple_choice, todos);
        todoList.setAdapter(adapter);
    }
}

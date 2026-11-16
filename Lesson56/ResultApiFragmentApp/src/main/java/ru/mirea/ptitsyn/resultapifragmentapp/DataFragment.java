package ru.mirea.ptitsyn.resultapifragmentapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.widget.EditText;
import androidx.fragment.app.FragmentManager;

public class DataFragment extends Fragment {
    private FragmentListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!(context instanceof FragmentListener)) {
            throw new IllegalStateException("Activity must implement FragmentListener");
        }
        listener = (FragmentListener) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_data, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText input = view.findViewById(R.id.editTextInfo);
        view.findViewById(R.id.buttonOpenBottomSheet).setOnClickListener(button -> {
            FragmentManager manager = getChildFragmentManager();
            if (manager.findFragmentByTag("ModalBottomSheet") != null || manager.isStateSaved()) return;
            Bundle result = new Bundle();
            result.putString("key", input.getText().toString());
            manager.setFragmentResult("requestKey", result);
            new BottomSheetFragment().show(manager, "ModalBottomSheet");
        });
        view.findViewById(R.id.buttonSendToActivity).setOnClickListener(button -> {
            listener.sendResult(input.getText().toString());
        });
    }

    @Override
    public void onDetach() {
        listener = null;
        super.onDetach();
    }
}

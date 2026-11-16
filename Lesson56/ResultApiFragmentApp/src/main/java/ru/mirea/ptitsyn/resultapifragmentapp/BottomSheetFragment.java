package ru.mirea.ptitsyn.resultapifragmentapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private String receivedText = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) receivedText = savedInstanceState.getString("received_text", "");
        getParentFragmentManager().setFragmentResultListener("requestKey",
                this,
                (requestKey, bundle) -> {
            receivedText = bundle.getString("key", "");
            Log.d("BottomSheetFragment", "Get text " + receivedText);
            displayText();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        displayText();
        view.findViewById(R.id.closeButton).setOnClickListener(button -> dismiss());
    }

    private void displayText() {
        View view = getView();
        if (view == null) return;
        TextView text = view.findViewById(R.id.receivedText);
        text.setText(receivedText.isEmpty() ? getString(R.string.empty_text) : receivedText);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("received_text", receivedText);
        super.onSaveInstanceState(outState);
    }
}

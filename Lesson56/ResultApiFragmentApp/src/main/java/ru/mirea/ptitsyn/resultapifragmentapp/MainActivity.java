package ru.mirea.ptitsyn.resultapifragmentapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements FragmentListener {
    private String lastResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true)
                    .add(R.id.dataContainer, DataFragment.class, null).commit();
        } else {
            lastResult = savedInstanceState.getString("activity_result");
            if (lastResult != null) showResult();
        }
    }

    @Override
    public void sendResult(String message) {
        lastResult = message;
        Log.d("MainActivity", "message: " + message);
        showResult();
    }

    private void showResult() {
        ((TextView) findViewById(R.id.activityResult)).setText(getString(R.string.activity_result, lastResult));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("activity_result", lastResult);
        super.onSaveInstanceState(outState);
    }
}

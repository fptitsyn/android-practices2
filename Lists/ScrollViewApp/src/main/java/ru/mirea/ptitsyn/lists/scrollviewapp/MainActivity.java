package ru.mirea.ptitsyn.lists.scrollviewapp;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.LinearLayout;
import android.widget.TextView;
import java.math.BigInteger;

public final class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View root = findViewById(R.id.mainRoot);
        int left = root.getPaddingLeft();
        int top = root.getPaddingTop();
        int right = root.getPaddingRight();
        int bottom = root.getPaddingBottom();
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets bars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(left + bars.left, top + bars.top, right + bars.right, bottom + bars.bottom);
            return windowInsets;
        });
        ViewCompat.requestApplyInsets(root);

        LinearLayout wrapper = findViewById(R.id.wrapper);
        BigInteger value = BigInteger.ONE;

        for (int number = 1; number <= 100; number++) {
            TextView item = (TextView) getLayoutInflater().inflate(R.layout.item, wrapper, false);
            item.setText(getString(R.string.progression_item, number, value.toString()));
            wrapper.addView(item);
            value = value.multiply(BigInteger.valueOf(2));
        }
    }
}

package ru.mirea.ptitsyn.lesson5;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String BASE_URL = "https://jsonplaceholder.typicode.com/";

    private final List<Call<?>> activeCalls = new ArrayList<>();
    private RecyclerView recyclerView;
    private TodoAdapter todoAdapter;
    private ApiService apiService;
    private ProgressBar progressBar;
    private TextView messageText;
    private Spinner sizeSpinner;
    private Spinner scaleSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (view, insets) -> {
            Insets bars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        messageText = findViewById(R.id.messageText);
        sizeSpinner = findViewById(R.id.sizeSpinner);
        scaleSpinner = findViewById(R.id.scaleSpinner);
        sizeSpinner.setSelection(1);

        AdapterView.OnItemSelectedListener imageOptionsListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyImageOptions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        };
        sizeSpinner.setOnItemSelectedListener(imageOptionsListener);
        scaleSpinner.setOnItemSelectedListener(imageOptionsListener);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        apiService = retrofit.create(ApiService.class);
        loadTodos();
    }

    private void loadTodos() {
        progressBar.setVisibility(View.VISIBLE);
        messageText.setText(R.string.loading);
        Call<List<Todo>> call = apiService.getTodos();
        activeCalls.add(call);
        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(@NonNull Call<List<Todo>> call, @NonNull Response<List<Todo>> response) {
                activeCalls.remove(call);
                if (isDestroyed() || isFinishing()) return;
                progressBar.setVisibility(View.GONE);
                if (!response.isSuccessful() || response.body() == null) {
                    messageText.setText(getString(R.string.load_http_error, response.code()));
                    Log.e(TAG, "getTodos HTTP " + response.code());
                    return;
                }
                List<Todo> todos = response.body();
                todoAdapter = new TodoAdapter(MainActivity.this, todos, MainActivity.this::updateTodo);
                recyclerView.setAdapter(todoAdapter);
                applyImageOptions();
                messageText.setText(todos.isEmpty() ? R.string.empty_list : R.string.server_notice);
            }

            @Override
            public void onFailure(@NonNull Call<List<Todo>> call, @NonNull Throwable throwable) {
                activeCalls.remove(call);
                if (call.isCanceled() || isDestroyed() || isFinishing()) return;
                progressBar.setVisibility(View.GONE);
                messageText.setText(R.string.load_network_error);
                Log.e(TAG, "getTodos failed", throwable);
            }
        });
    }

    private void updateTodo(Todo todo, boolean completed) {
        boolean previous = Boolean.TRUE.equals(todo.getCompleted());
        todo.setCompleted(completed);
        todoAdapter.setUpdating(todo, true);
        Call<Todo> call = apiService.updateTodo(todo.getId(), Collections.singletonMap("completed", completed));
        activeCalls.add(call);
        call.enqueue(new Callback<Todo>() {
            @Override
            public void onResponse(@NonNull Call<Todo> call, @NonNull Response<Todo> response) {
                activeCalls.remove(call);
                if (isDestroyed() || isFinishing()) return;
                Todo updated = response.body();
                if (response.isSuccessful() && updated != null && updated.getCompleted() != null) {
                    todo.setCompleted(updated.getCompleted());
                    Toast.makeText(MainActivity.this, R.string.update_success, Toast.LENGTH_SHORT).show();
                } else {
                    todo.setCompleted(previous);
                    String message = getString(R.string.update_http_error, response.code());
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                    Log.e(TAG, "updateTodo HTTP " + response.code());
                }
                todoAdapter.setUpdating(todo, false);
            }

            @Override
            public void onFailure(@NonNull Call<Todo> call, @NonNull Throwable throwable) {
                activeCalls.remove(call);
                if (call.isCanceled() || isDestroyed() || isFinishing()) return;
                todo.setCompleted(previous);
                todoAdapter.setUpdating(todo, false);
                Toast.makeText(MainActivity.this, R.string.update_network_error, Toast.LENGTH_LONG).show();
                Log.e(TAG, "updateTodo failed", throwable);
            }
        });
    }

    private void applyImageOptions() {
        if (todoAdapter == null) return;
        int[] sizes = {64, 96, 128};
        int size = sizes[sizeSpinner.getSelectedItemPosition()];
        todoAdapter.setImageOptions(size, scaleSpinner.getSelectedItemPosition() == 0);
    }

    @Override
    protected void onDestroy() {
        for (Call<?> call : activeCalls) call.cancel();
        activeCalls.clear();
        if (todoAdapter != null) Picasso.get().cancelTag(todoAdapter);
        super.onDestroy();
    }
}

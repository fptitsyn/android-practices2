package ru.mirea.ptitsyn.lesson5;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface ApiService {
    @GET("todos")
    Call<List<Todo>> getTodos();

    @PATCH("todos/{id}")
    Call<Todo> updateTodo(@Path("id") int id, @Body Map<String, Boolean> changes);
}

package ru.mirea.ptitsyn.lesson5;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TodoAdapter extends RecyclerView.Adapter<TodoViewHolder> {
    public interface OnCompletedChangedListener {
        void onCompletedChanged(Todo todo, boolean completed);
    }

    private final LayoutInflater layoutInflater;
    private final List<Todo> todos;
    private final OnCompletedChangedListener listener;
    private final Set<Integer> updatingIds = new HashSet<>();
    private int imageSizeDp = 96;
    private boolean centerCrop = true;

    public TodoAdapter(Context context, List<Todo> todos, OnCompletedChangedListener listener) {
        layoutInflater = LayoutInflater.from(context);
        this.todos = todos;
        this.listener = listener;
    }

    public void setImageOptions(int imageSizeDp, boolean centerCrop) {
        this.imageSizeDp = imageSizeDp;
        this.centerCrop = centerCrop;
        notifyDataSetChanged();
    }

    public void setUpdating(Todo todo, boolean updating) {
        if (updating) updatingIds.add(todo.getId());
        else updatingIds.remove(todo.getId());
        int position = todos.indexOf(todo);
        if (position != -1) notifyItemChanged(position);
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        boolean completed = Boolean.TRUE.equals(todo.getCompleted());
        holder.textViewTitle.setText(todo.getTitle());

        // При переиспользовании строки setChecked не должен отправлять новый запрос.
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        holder.checkBoxCompleted.setChecked(completed);
        holder.checkBoxCompleted.setEnabled(!updatingIds.contains(todo.getId()));
        int status = completed ? R.string.completed : R.string.not_completed;
        holder.checkBoxCompleted.setContentDescription(holder.itemView.getContext().getString(status));
        holder.checkBoxCompleted.setOnCheckedChangeListener((button, checked) -> {
            listener.onCompletedChanged(todo, checked);
        });

        float density = holder.itemView.getResources().getDisplayMetrics().density;
        int imageSizePx = Math.round(imageSizeDp * density);
        ViewGroup.LayoutParams params = holder.imageViewTodo.getLayoutParams();
        params.width = imageSizePx;
        params.height = imageSizePx;
        holder.imageViewTodo.setLayoutParams(params);

        // Todo не содержит изображений: для каждого id используем отдельную иллюстрацию Picsum.
        String imageUrl = "https://picsum.photos/seed/todo" + todo.getId() + "/400/300";
        RequestCreator request = Picasso.get().load(imageUrl).tag(this)
                .placeholder(R.drawable.image_placeholder).error(R.drawable.image_error);
        request.resize(imageSizePx, imageSizePx);
        if (centerCrop) request.centerCrop();
        else request.centerInside();
        request.into(holder.imageViewTodo);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    @Override
    public void onViewRecycled(@NonNull TodoViewHolder holder) {
        Picasso.get().cancelRequest(holder.imageViewTodo);
        holder.checkBoxCompleted.setOnCheckedChangeListener(null);
        super.onViewRecycled(holder);
    }
}

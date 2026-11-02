package ru.mirea.ptitsyn.lesson5;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TodoViewHolder extends RecyclerView.ViewHolder {
    final TextView textViewTitle;
    final CheckBox checkBoxCompleted;
    final ImageView imageViewTodo;

    public TodoViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTitle = itemView.findViewById(R.id.textViewTitle);
        checkBoxCompleted = itemView.findViewById(R.id.checkBoxCompleted);
        imageViewTodo = itemView.findViewById(R.id.imageViewTodo);
    }
}

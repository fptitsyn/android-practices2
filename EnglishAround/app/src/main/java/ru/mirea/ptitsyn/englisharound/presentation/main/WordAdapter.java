package ru.mirea.ptitsyn.englisharound.presentation.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.ptitsyn.domain.models.Word;
import ru.mirea.ptitsyn.englisharound.R;

public final class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    public interface OnWordClickListener {
        void onWordClick(Word word);
    }

    private final List<Word> items = new ArrayList<>();
    private final OnWordClickListener listener;

    public WordAdapter(OnWordClickListener listener) {
        this.listener = listener;
    }

    public void setItems(List<Word> words) {
        List<Word> updatedItems = words == null ? new ArrayList<>() : new ArrayList<>(words);
        items.clear();
        items.addAll(updatedItems);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_word, parent, false);
        return new WordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WordViewHolder holder, int position) {
        Word word = items.get(position);
        holder.bind(word);
        holder.itemView.setOnClickListener(view -> listener.onWordClick(word));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static final class WordViewHolder extends RecyclerView.ViewHolder {

        private final TextView englishText;
        private final TextView translationText;

        public WordViewHolder(@NonNull View itemView) {
            super(itemView);
            englishText = itemView.findViewById(R.id.wordEnglishText);
            translationText = itemView.findViewById(R.id.wordTranslationText);
        }

        public void bind(Word word) {
            englishText.setText(word.getEnglish());
            translationText.setText(word.getTranslation());
        }
    }
}
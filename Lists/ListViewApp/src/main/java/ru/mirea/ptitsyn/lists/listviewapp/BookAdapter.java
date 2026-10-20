package ru.mirea.ptitsyn.lists.listviewapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.List;

public final class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(Context context, List<Book> books) {
        super(context, R.layout.item_book, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_book, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Book book = getItem(position);
        if (book != null) {
            holder.title.setText((position + 1) + ". " + book.getTitle());
            holder.author.setText(book.getAuthor());
        }
        return convertView;
    }

    private static final class ViewHolder {
        final TextView title;
        final TextView author;

        ViewHolder(View view) {
            title = view.findViewById(R.id.bookTitle);
            author = view.findViewById(R.id.bookAuthor);
        }
    }
}

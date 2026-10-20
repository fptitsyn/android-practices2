package ru.mirea.ptitsyn.lists.recyclerviewapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public final class HistoricalEventAdapter extends RecyclerView.Adapter<HistoricalEventAdapter.EventViewHolder> {
    private final List<HistoricalEvent> events;

    public HistoricalEventAdapter(List<HistoricalEvent> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(events.get(position));
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public static final class EventViewHolder extends RecyclerView.ViewHolder {
        private final ImageView image;
        private final TextView date;
        private final TextView title;
        private final TextView description;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.eventImage);
            date = itemView.findViewById(R.id.eventDate);
            title = itemView.findViewById(R.id.eventTitle);
            description = itemView.findViewById(R.id.eventDescription);
        }

        public void bind(HistoricalEvent event) {
            image.setImageResource(event.getImageResId());
            date.setText(event.getDate());
            title.setText(event.getTitle());
            description.setText(event.getDescription());
            itemView.setOnClickListener(view -> {
                String message = view.getContext().getString(R.string.event_clicked, event.getTitle());
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
            });
        }
    }
}

package com.example.qrconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class SinglePosterAdapter extends ArrayAdapter<Event>  {
    private List<Event> eventsList;

    public SinglePosterAdapter(Context context, List<Event> events) {
        super(context, 0, events);
        this.eventsList = events;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(getContext()).inflate(R.layout.user_single_poster_layout, parent, false);
        }

        // Get the current event
        Event currentEvent = eventsList.get(position);

        // Set event title
        TextView eventTitleTextView = itemView.findViewById(R.id.eventTitle);
        eventTitleTextView.setText(currentEvent.getEventTitle());

        // Load event poster using Picasso library
        ImageView eventPosterImageView = itemView.findViewById(R.id.eventPoster);
        if (currentEvent.getEventPosterUrl() != null && !currentEvent.getEventPosterUrl().isEmpty()) {
            Glide.with(SinglePosterAdapter.this.getContext())
                    .load(currentEvent.getEventPosterUrl())
                    .into(eventPosterImageView);
        }

        return itemView;
    }


}

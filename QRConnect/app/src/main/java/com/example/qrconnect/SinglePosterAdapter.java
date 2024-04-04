package com.example.qrconnect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class SinglePosterAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> eventsList;
    private Context context;
    private FirebaseFirestore db;

    public SinglePosterAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.context = context;
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
//        loadEventPoster(currentEvent, eventPosterImageView);

        // Check if the event has a posterURL
        if (currentEvent.getEventPosterUrl() != null && !currentEvent.getEventPosterUrl().isEmpty()) {
            // Event has a poster, load the image using Glide
            Glide.with(context)
                    .load(currentEvent.getEventPosterUrl())
                    .into(eventPosterImageView);
        } else {
            // Event does not have a poster, load placeholder image
            eventPosterImageView.setImageResource(R.drawable.placeholder_image);
        }
        return itemView;
    }
}

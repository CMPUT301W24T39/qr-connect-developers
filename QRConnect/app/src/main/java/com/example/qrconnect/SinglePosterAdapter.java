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

/**
 * This class maintains the functions of displaying poster
 */
public class SinglePosterAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> eventsList;
    private Context context;
    private FirebaseFirestore db;

    /**
     * The constructor of adapting the list of events
     * @param context
     * @param events
     */
    public SinglePosterAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.eventsList = events;
    }

    /**
     * The view of the list
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return the view
     */
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

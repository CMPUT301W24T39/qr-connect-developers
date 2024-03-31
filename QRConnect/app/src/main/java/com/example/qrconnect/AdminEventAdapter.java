package com.example.qrconnect;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * The AdminEventAdapter class makes the objects in adminEventDataList to be able to show in the view.
 * It extends the ArrayAdapter class.
 */
public class AdminEventAdapter extends ArrayAdapter<Event> {
    private ArrayList<Event> events;
    private Context context;

    /**
     * Constructs a AdminEventAdapter with the specified details.
     * @param context context for the AdminEventAdapter.
     * @param events events for the AdminEventAdapter.
     */
    public AdminEventAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    /**
     * This makes the events show in the view.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to
     * @return Return the view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.list_event_organizer, parent,false);
        }

        Event event = events.get(position);
        TextView eventTitle = view.findViewById(R.id.event_title_text);
        eventTitle.setText(event.getEventTitle());

        return view;
    }
}

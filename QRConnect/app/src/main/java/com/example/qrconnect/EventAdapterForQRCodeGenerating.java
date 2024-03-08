package com.example.qrconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * The EventAdapterForQRCodeGenerating class creates an adapter for displaying events in a list.
 * This adapter is designed for generating QR codes for events.
 * It extends ArrayAdapter<Event>.
 */
public class EventAdapterForQRCodeGenerating extends ArrayAdapter<Event> {
    /**
     * Constructs an EventAdapterForQRCodeGenerating object.
     * @param context The context in which the adapter is being used.
     * @param events The list of events to be displayed.
     */
    public EventAdapterForQRCodeGenerating(Context context, List<Event> events) {
        super(context, 0, events);
    }

    /**
     * Gets a View that displays the data at the specified position in the list.
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return The View corresponding to the data at the specified position in the list.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_event, parent, false);
        } else {
            view = convertView;
        }

        Event event = getItem(position);
        TextView eventTitle = view.findViewById(R.id.event_title_text);

        eventTitle.setText(event.getEventTitle());


        return view;
    }

    /**
     * Gets a View that displays the data in a dropdown view.
     * @param position index of the item whose view we want.
     * @param convertView the old view to reuse, if possible. Note: You should
     *        check that this view is non-null and of an appropriate type before
     *        using. If it is not possible to convert this view to display the
     *        correct data, this method can create a new view.
     * @param parent the parent that this view will eventually be attached to
     * @return The View corresponding to specified position in the dropdown view data display.
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_event, parent, false);
        } else {
            view = convertView;
        }

        Event event = getItem(position);
        TextView eventTitle = view.findViewById(R.id.event_title_text);

        eventTitle.setText(event.getEventTitle());


        return view;
    }
}

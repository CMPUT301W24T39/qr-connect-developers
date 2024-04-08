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
import java.util.List;

/**
 * The EventAdapter class makes the objects in eventDataList to be able to show in the view.
 * It extends the ArrayAdapter class.
 */
public class EventAdapter extends ArrayAdapter<Event> {
    private Context context;
    private ArrayList<Event> events;

    public EventAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    /**
     * This makes the title of an event to show in the view.
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return Return the view.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        Event event = events.get(position);
        String userId = UserPreferences.getUserId(EventAdapter.this.getContext());
        String hostId = event.getHostId();

        if (userId.equals(hostId)) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_event_organizer, parent, false);
        }
        else {
            //check-in or signup
            if (event.isAttendeeInThisEvent(userId)) {
                view = LayoutInflater.from(getContext()).inflate(R.layout.list_event_checkin, parent, false);
            } else {
                view = LayoutInflater.from(getContext()).inflate(R.layout.list_event_signup, parent, false);

            }
        }


        TextView eventTitle = view.findViewById(R.id.event_title_text);

        eventTitle.setText(event.getEventTitle());

        return view;
    }

    /**
     * This makes the title of an event to show in the drop down view.
     * @param position index of the item whose view we want.
     * @param convertView the old view to reuse, if possible. Note: You should
     *        check that this view is non-null and of an appropriate type before
     *        using. If it is not possible to convert this view to display the
     *        correct data, this method can create a new view.
     * @param parent the parent that this view will eventually be attached to.
     * @return Return the view.
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.list_event, parent, false);
        } else {
            view = convertView;
        }

        Event event = events.get(position);
        TextView eventTitle = view.findViewById(R.id.event_title_text);

        eventTitle.setText(event.getEventTitle());
        return view;
    }
}

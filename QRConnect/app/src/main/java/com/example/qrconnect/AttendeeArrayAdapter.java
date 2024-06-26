package com.example.qrconnect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * The AttendeeArrayAdapter class provides a way to adapt an ArrayList of Attendee objects
 * to be displayed in an AdapterView.
 * It extends the ArrayAdapter Class.
 */
public class AttendeeArrayAdapter extends ArrayAdapter<DisplayAttendee> {

    private ArrayList<DisplayAttendee> attendees;
    private Context context;
    private String eventId;

    /**
     * Constructs an instance of the AttendeeArrayAdapter.
     * @param context context for AttendeeArrayAdapter.
     * @param attendees attendees for AttendeeArrayAdapter.
     * @param eventId eventID for AttendeeArrayAdapter.
     */
    public AttendeeArrayAdapter(Context context, ArrayList<DisplayAttendee> attendees, String eventId) {
        super(context, 0, attendees);
        this.attendees = attendees;
        this.context = context;
        this.eventId = eventId;
    }

    /**
     * Returns a view for each item in the AdapterView.
     * @param position The position of the item within the adapter's data set of the item whose view we want.
     * @param convertView The old view to reuse, if possible.
     * @param parent The parent that this view will eventually be attached to.
     * @return A View corresponding to the data at the specified position.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_content, parent,false);
        }

        DisplayAttendee attendee = attendees.get(position);
        TextView attendeeName = view.findViewById(R.id.attendee_name_text);
        TextView attendeeCheckInCount = view.findViewById(R.id.attendee_sign_in_count);

        attendeeName.setText(attendee.getUserName());
        if (attendee.getCheckInCount(eventId) == 0L){
            Log.d("Attendee Array Adapter", "Adapt for displaying signup User");
            attendeeCheckInCount.setText("");
        } else {
            attendeeCheckInCount.setText(String.valueOf(attendee.getCheckInCount(eventId)));
        }

        return view;
    }
}
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

public class AttendeeArrayAdapter extends ArrayAdapter<Attendee> {

    private ArrayList<Attendee> attendees;
    private Context context;
    private Integer eventId;
    public AttendeeArrayAdapter(Context context, ArrayList<Attendee> attendees, Integer eventId) {
        super(context, 0, attendees);
        this.attendees = attendees;
        this.context = context;
        this.eventId = eventId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.attendee_content, parent,false);
        }

        Attendee attendee = attendees.get(position);

        TextView attendeeName = view.findViewById(R.id.attendee_name_text);
        TextView attendeeCheckInCount = view.findViewById(R.id.attendee_sign_in_count);

        attendeeName.setText(attendee.getName());
        attendeeCheckInCount.setText(String.valueOf(attendee.getCheckInCount(eventId)));

        return view;

    }

}

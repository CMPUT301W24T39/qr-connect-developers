package com.example.qrconnect;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
    }

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

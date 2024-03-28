package com.example.qrconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The AttendeeListActivity class represents an activity that displays a list of attendees.
 * It extends the AppCompatActivity class.
 */
public class AttendeeListActivity extends AppCompatActivity {
    private ArrayList<User> attendees;
    private AttendeeArrayAdapter adapter;

    /**
     * Called when the activity is starting. This is where most initialization should go.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_attendee_list);
        Event currentEvent = (Event) getIntent().getSerializableExtra("EVENT");
        attendees = generateAttendees(currentEvent);
        ListView attendeeListView = findViewById(R.id.show_attendee_list_view);
        ImageButton backButton = findViewById(R.id.attendee_list_back_nav_button);

        //Display total attendee
        TextView currentAttendees = findViewById(R.id.attendee_list_count_current_attendance);
        currentAttendees.setText(String.valueOf(attendees.size()));

        // Create adapter and set it to the ListView
        adapter = new AttendeeArrayAdapter(this, attendees, currentEvent.getEventId());
        attendeeListView.setAdapter(adapter);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    /**
     * Generates attendees for displaying.
     * @param event  the event related.
     * @return the list of mock attendees.
     */
    private ArrayList<User> generateAttendees(Event event) {
        ArrayList<User> displayAttendees = new ArrayList<>();

        HashMap<String, String> attendeeListIdToName = event.getAttendeeListIdToName();
        HashMap<String, Long> attendeeListIdToCheckInTimes = event.getAttendeeListIdToCheckInTimes();

        for (Map.Entry<String, String> entry : attendeeListIdToName.entrySet()) {
            String userId = entry.getKey();
            String userName = entry.getValue();

            Long checkInCount = attendeeListIdToCheckInTimes.getOrDefault(userId, 0L);

            User attendee = new User(userId, userName, null);
            attendee.updateCheckInCount(event.getEventId(), checkInCount);

            displayAttendees.add(attendee);
        }

        return displayAttendees;
    }
}

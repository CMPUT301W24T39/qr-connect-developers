package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

/**
 * The AttendeeListActivity class represents an activity that displays a list of attendees.
 * It extends the AppCompatActivity class.
 */
public class AttendeeListActivity extends AppCompatActivity {
    private ArrayList<Attendee> attendees;
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

        attendees = generateMockAttendees();

        ListView attendeeListView = findViewById(R.id.show_attendee_list_view);

        ImageButton backButton = findViewById(R.id.attendee_list_back_nav_button);

        // Create adapter and set it to the ListView
        adapter = new AttendeeArrayAdapter(this, attendees, 000000);
        attendeeListView.setAdapter(adapter);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Generates mock attendees for testing purposes.
     * @return the list of mock attendees.
     */
    private ArrayList<Attendee> generateMockAttendees() {
        ArrayList<Attendee> mockAttendees = new ArrayList<>();
        // Add mock attendees
        mockAttendees.add(new Attendee("1", "John Doe", null));
        mockAttendees.add(new Attendee("2", "Jane Smith", null));
        mockAttendees.add(new Attendee("3", "Alice Johnson", null));
        mockAttendees.add(new Attendee("4", "Bob Brown", null));
        mockAttendees.add(new Attendee("5", "Emma Wilson", null));
        mockAttendees.add(new Attendee("6", "Michael Davis", null));
        mockAttendees.add(new Attendee("7", "Emily Anderson", null));
        mockAttendees.add(new Attendee("8", "David Miller", null));
        mockAttendees.add(new Attendee("9", "Sarah Garcia", null));
        mockAttendees.add(new Attendee("10", "Ryan Martinez", null));
        // Add more attendees as needed
        return mockAttendees;
    }
}

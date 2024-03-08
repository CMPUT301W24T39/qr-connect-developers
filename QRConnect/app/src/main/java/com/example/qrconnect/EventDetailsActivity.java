package com.example.qrconnect;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * The EventDetailsAcitivty class displays details of a specific event.
 * It allows users to navigate back, access the menu, and send notifications for the event.
 * It extends AppCompatActivity.
 */
public class EventDetailsActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created. Responsible for initializing the activity.
     * @param savedInstanceState A Bundle containing the activity's previously frozen state, if there was one.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        EditText eventDescriptionEdit = findViewById(R.id.event_description_edit);
        EditText eventDate = findViewById(R.id.event_date);
        EditText eventTime = findViewById(R.id.event_time);
        EditText eventLocation = findViewById(R.id.event_location);
        EditText eventCapacity = findViewById(R.id.event_capacity);
        EditText eventCurrentAttendance = findViewById(R.id.event_current_attendance);
        Button shareEventButton = findViewById(R.id.share_event_button);
        Button saveChangesButton = findViewById(R.id.save_event_button);

        String eventId = getIntent().getStringExtra("EVENT_ID");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventId);

        loadEventData(eventRef, eventDescriptionEdit, eventDate, eventTime, eventLocation, eventCapacity, eventCurrentAttendance);

        saveChangesButton.setOnClickListener(v -> {
            // Gather data from UI components
            String description = eventDescriptionEdit.getText().toString();
            String date = eventDate.getText().toString();
            String time = eventTime.getText().toString();
            String location = eventLocation.getText().toString();
            int capacity = Integer.parseInt(eventCapacity.getText().toString());
            int currentAttendance = Integer.parseInt(eventCurrentAttendance.getText().toString());

            // Create or update event object
            Map<String, Object> event = new HashMap<>();
            event.put("description", description);
            event.put("date", date);
            event.put("time", time);
            event.put("location", location);
            event.put("capacity", capacity);
            event.put("currentAttendance", currentAttendance);

            // Save or update the event in Firestore
            eventRef.set(event)
                    .addOnSuccessListener(aVoid -> Toast.makeText(EventDetailsActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(EventDetailsActivity.this, "Error updating event", Toast.LENGTH_SHORT).show());
        });

        // initialize backButton
        ImageButton backButton = findViewById(R.id.event_details_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // initialize menu button
        ImageButton menuButton = findViewById(R.id.event_details_menu_icon_button);

        menuButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Intent showIntent = new Intent(EventDetailsActivity.this, AttendeeListActivity.class);
                    //TODO: showIntent.putExtra("EVENT", event);
                    startActivity(showIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

        // initialize send notification button
        ImageButton sendNotificationsButton = findViewById(R.id.event_details_send_notifications);

        sendNotificationsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Intent showIntent = new Intent(EventDetailsActivity.this, SendNotificationsActivity.class);
                    //TODO: showIntent.putExtra("EVENT", event);
                    startActivity(showIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });

    }
    private void loadEventData(DocumentReference eventRef,
                               EditText eventDescriptionEdit,
                               EditText eventDate,
                               EditText eventTime,
                               EditText eventLocation,
                               EditText eventCapacity,
                               EditText eventCurrentAttendance) {
        eventRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Directly set text on each EditText from the document
                    eventDescriptionEdit.setText(document.getString("description"));
                    eventDate.setText(document.getString("date"));
                    eventTime.setText(document.getString("time"));
                    eventLocation.setText(document.getString("location"));
                    eventCapacity.setText(String.valueOf(document.getLong("capacity")));
                    eventCurrentAttendance.setText(String.valueOf(document.getLong("currentAttendance")));
                } else {
                    Toast.makeText(EventDetailsActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(EventDetailsActivity.this, "Failed to load event", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

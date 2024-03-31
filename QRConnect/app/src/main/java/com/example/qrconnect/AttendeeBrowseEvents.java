package com.example.qrconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The AttendeeBrowseEvents class manages the user browse events by posters page.
 * It extends AppCompatActivity.
 */
public class AttendeeBrowseEvents extends AppCompatActivity {

    private ListView listView;
    private List<Event> eventsList;
    ImageButton backButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_browse_posters);

        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();

        // Retrieve the list of events from the intent extra
        eventsList = (List<Event>) getIntent().getSerializableExtra("events");
        // Filter out events without posters
        List<Event> eventsWithPosters = new ArrayList<>();
        // Initialize a counter to keep track of processed events
        AtomicInteger counter = new AtomicInteger(0);

        for (Event event : eventsList) {
            String eventId = event.getEventId();

            // Query Firestore to check if the event has a posterURL
            db.collection("events").document(eventId)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                String posterUrl = documentSnapshot.getString("posterURL");
                                if (posterUrl != null && !posterUrl.isEmpty()) {
                                    // Add event to the list of events with posters
                                    eventsWithPosters.add(event);
                                }
                            }
                            // Increment the counter and check if all events have been queried
                            if (counter.incrementAndGet() == eventsList.size()) {
                                // Update the ListView with events with posters
                                updateListView(eventsWithPosters);
                            }
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            // Log message for error
                        }
                    });
        }

        // set back button listener
        backButton = findViewById(R.id.user_posters_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        }

        // Method to update the ListView with events with posters
        private void updateListView (List < Event > eventsWithPosters) {
            listView = findViewById(R.id.eventPosterListView);
            SinglePosterAdapter adapter = new SinglePosterAdapter(AttendeeBrowseEvents.this, eventsWithPosters, db);
            listView.setAdapter(adapter);
        }
}
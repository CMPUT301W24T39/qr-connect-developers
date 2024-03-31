package com.example.qrconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * The AdminEventDetails class manages the admin even details page so the admin has the option to delete the event.
 * It extends AppCompatActivity.
 */
public class AdminEventDetails extends AppCompatActivity {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private DocumentReference eventRef;
    /**
     * Initializes the activity, sets the content view, and begins the process of loading event details.
     * It retrieves the event ID passed from the previous activity and uses it to load the corresponding
     * event details from Firestore and the event poster from Firebase Storage.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     *                           Otherwise, it is null.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String eventId = getIntent().getStringExtra("EVENT_ID");
        Log.d("Promo Details Activity", "Received event ID: " + eventId);
        setContentView(R.layout.admin_browse_events_event_details);
        loadEventDetails(eventId);

        // Back button to the admin event browse page
        ImageButton backButton = findViewById(R.id.admin_event_details_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    /**
     * Fetches and displays the details of an event including its title, description, date, time, location,
     * and capacity from Firestore. It also retrieves the event's poster image from Firebase Storage and displays
     * it using Glide.
     *
     * @param eventId The unique identifier of the event whose details are to be loaded and displayed.
     */
    private void loadEventDetails(String eventId){
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        eventRef = eventsRef.document(eventId);

        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Set text fields with data from Firestore
                TextView eventTitle = findViewById(R.id.admin_event_title_textview);
                TextView eventDescription = findViewById(R.id.admin_description_text);
                TextView eventDate = findViewById(R.id.admin_date_text);
                TextView eventTime = findViewById(R.id.admin_time_text);
                TextView eventAddress = findViewById(R.id.admin_location_text);
                TextView eventCapacity = findViewById(R.id.admin_capacity_text);
                ImageView eventPoster = findViewById(R.id.admin_event_image);

                eventTitle.setText(documentSnapshot.getString("title"));
                eventDescription.setText(documentSnapshot.getString("description"));
                eventDate.setText(documentSnapshot.getString("date"));
                eventTime.setText(documentSnapshot.getString("time"));
                eventAddress.setText(documentSnapshot.getString("location"));
                Long capacity = documentSnapshot.getLong("capacity");
                eventCapacity.setText(capacity != null ? String.valueOf(capacity) : "0");

                // Load the poster image from Firebase Storage
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("eventposters/" + eventId + "_eventPoster.jpg");

                // Use Glide or a similar library to load the image
                storageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    Glide.with(this)
                            .load(uri)
                            .into(eventPoster);
                }).addOnFailureListener(e -> {
                    Log.d("EventDetails", "Error loading image: ", e);
                });
            } else {
                Log.d("EventDetails", "Document does not exist.");
            }
        }).addOnFailureListener(e -> {
            Log.d("EventDetails", "Error fetching document: ", e);
        });
    }
}
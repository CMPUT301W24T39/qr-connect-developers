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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignupDetailsActivity extends AppCompatActivity {
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
        String eventId = getIntent().getStringExtra("eventId");
        Log.d("Signup Details Activity", "Received event ID: " + eventId);
        setContentView(R.layout.event_details_signup);
        loadSignupDetails(eventId);
        signupBackAction();
    }
    /**
     * Fetches and displays the details of an event including its title, description, date, time, location,
     * and capacity from Firestore. It also retrieves the event's poster image from Firebase Storage and displays
     * it using Glide.
     *
     * @param eventId The unique identifier of the event whose details are to be loaded and displayed.
     */
    private void loadSignupDetails(String eventId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(eventId);

        eventRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Set text fields with data from Firestore
                TextView eventTitle = findViewById(R.id.promo_event_title_textview_signup);
                TextView eventDescription = findViewById(R.id.promo_description_text_signup);
                TextView eventDate = findViewById(R.id.promo_date_text_signup);
                TextView eventTime = findViewById(R.id.promo_time_text_signup);
                TextView eventAddress = findViewById(R.id.promo_location_text_signup);
                TextView eventCapacity = findViewById(R.id.promo_capacity_text_signup);
                ImageView eventPoster = findViewById(R.id.promo_event_image_signup);
                TextView eventCurrentAttendance = findViewById(R.id.promo_event_current_attendance_text_signup);

                eventTitle.setText(documentSnapshot.getString("title"));
                eventDescription.setText(documentSnapshot.getString("description"));
                eventDate.setText(documentSnapshot.getString("date"));
                eventTime.setText(documentSnapshot.getString("time"));
                eventAddress.setText(documentSnapshot.getString("location"));
                Long capacity = documentSnapshot.getLong("capacity");
                eventCapacity.setText(capacity != null ? String.valueOf(capacity) : "0");
                Long attendance = documentSnapshot.getLong("currentAttendance");
                eventCurrentAttendance.setText(attendance != null ? String.valueOf(attendance) : "0");

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
    /**
     * Sets up the action for the back navigation button. When clicked, it finishes the current activity,
     * effectively navigating the user back to the previous screen.
     */
    private void signupBackAction(){
        ImageButton backButton = findViewById(R.id.back_nav_button_signup);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
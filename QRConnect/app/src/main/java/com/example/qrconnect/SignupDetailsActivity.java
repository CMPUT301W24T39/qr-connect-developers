package com.example.qrconnect;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

/**
 * The class maintains the functions of displaying event details when a user wants to sign up an event
 */
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

    // define attributes
    private FirebaseFirestore db;
    private String eventId;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getIntent().getStringExtra("eventId");
        Log.d("Signup Details Activity", "Received event ID: " + eventId);
        setContentView(R.layout.event_details_signup);
        loadSignupDetails(eventId);

        setupSignupButton();

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
    private void setupSignupButton(){
        Button signup_button = findViewById(R.id.signup_details_signup_button);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // add user to signup
                String userId = UserPreferences.getUserId(getApplicationContext());
                db = FirebaseFirestore.getInstance();
                DocumentReference eventRef = db.collection("events").document(eventId);

                eventRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        DocumentReference userRef = db.collection("users").document(userId);
                        // get user's name
                        userRef.get().addOnSuccessListener(userDocument -> {
                            if (userDocument.exists()) {
                                String firstName = userDocument.getString("firstName");
                                String lastName = userDocument.getString("lastName");
                                String currentUserName;
                                // Check if firstName or lastName are null and replace with empty string if so
                                if (firstName == null && lastName == null) {
                                    // If both are null, set currentUserName to "anonymous"
                                    currentUserName = "Anonymous";
                                } else {
                                    // If only one of them is null, replace it with an empty string
                                    if (firstName == null) {
                                        firstName = "";
                                    }
                                    if (lastName == null) {
                                        lastName = "";
                                    }
                                    currentUserName = firstName + " " + lastName;
                                }

                                // Create a new hashmap to update the signupUserIdToName field
                                HashMap<String, String> updatedSignupMap = new HashMap<>();
                                // Get the existing signupUserIdToName hashmap from Firestore
                                HashMap<String, String> existingSignupMap = (HashMap<String, String>) documentSnapshot.get("signupUserIdToName");
                                if (existingSignupMap != null) {
                                    updatedSignupMap.putAll(existingSignupMap);
                                }
                                updatedSignupMap.put(userId, currentUserName);

                                // Update the signupUserIdToName field in Firestore
                                eventRef.update("signupUserIdToName", updatedSignupMap)
                                        .addOnSuccessListener(aVoid -> Log.d("SignupDetailsActivity", "User signed up successfully for event"))
                                        .addOnFailureListener(e -> Log.e("SignupDetailsActivity", "Error signing up user: " + e.getMessage()));
                                showSignUpSuccessfullyDialog();
                            } else {
                                Log.e("SignupDetailsActivity", "User document does not exist in the users collection.");
                            }
                        }).addOnFailureListener(e -> {
                            Log.e("SignupDetailsActivity", "Error fetching user document: " + e.getMessage());
                        });
                    } else {
                        Log.e("SignupDetailsActivity", "Event document does not exist.");
                    }
                }).addOnFailureListener(e -> {
                    Log.e("SignupDetailsActivity", "Error fetching event document: " + e.getMessage());
                });
            }
        });
    }

    private void showSignUpSuccessfullyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Alert");
        builder.setMessage("Sign up was successful.");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

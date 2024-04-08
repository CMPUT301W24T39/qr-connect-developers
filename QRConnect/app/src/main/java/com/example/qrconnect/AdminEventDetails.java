package com.example.qrconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

/**
 * The AdminEventDetails class manages the admin even details page so the admin has the option to delete the event.
 * It extends AppCompatActivity.
 */
public class AdminEventDetails extends AppCompatActivity implements AdminDeleteEventFragment.AdminDeleteEventDialogListener {
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
        setContentView(R.layout.admin_browse_events_event_details);

        // Get items from previous activity
        Event currentEvent = (Event) getIntent().getSerializableExtra("EVENT");
        Log.d("Admin Browse Events", "Received event: " + currentEvent);
        String eventId = currentEvent.getEventId();

        // Get event details for the event that was clicked on
        loadEventDetails(eventId);

        // Back button to the admin event browse page
        ImageButton backButton = findViewById(R.id.admin_event_details_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Delete button
        Button deleteButton = findViewById(R.id.admin_delete_event_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager() != null) {
                    new AdminDeleteEventFragment(currentEvent, AdminEventDetails.this).show(getSupportFragmentManager(), "Delete Event");
            }   }
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

    /**
     * This deletes an event from the firebase.
     * @param event This is the event to delete.
     */
    public void deleteEvent(Event event){
        String eventId = event.getEventId();

        eventsRef
                .document(event.getEventId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error deleting document", e);
                    }
                });

        if (event.getEventCheckInId() != null && event.getEventPromoId() != null){
            String qrCodeFilePath = "qrcodes/" + event.getEventId() + "_" + "checkInQRCodeImageUrl" + ".jpg";
            String promoQrCodeFilePath = "qrcodes/" + event.getEventId() + "_" + "promoQRCodeImageUrl" + ".jpg";
            String posterImagePath = "eventposters/" + event.getEventId() + "_eventPoster.jpg";
            deleteQRCodesFromStorage(qrCodeFilePath);
            deleteQRCodesFromStorage(promoQrCodeFilePath);

            deleteDataByName(event.getEventId());
            checkAndDeleteImage(posterImagePath);
        }
        finish();
    }

    /**
     * Delete the QR codes from Storage
     * @param filePath the path of the file
     */
    private void deleteQRCodesFromStorage(String filePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference().child(filePath);

        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Storage", "File successfully deleted!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Storage", "Error deleting file", e);
            }
        });
    }

    /**
     * This method checks if the image is in the path "eventposters/" or "profile_pictures"
     * @param imagePath the path of the image
     */
    private void checkAndDeleteImage(String imagePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child(imagePath);

        imageRef.delete().addOnSuccessListener(aVoid -> {
            Log.d("DeleteImage", imagePath + " has been deleted from Firebase Storage.");
        }).addOnFailureListener(exception -> {
            Log.d("DeleteImage", "Failed to delete " + imagePath + " from Firebase Storage: " + exception.getMessage());
        });
    }

    /**
     * Delete the image using its name as the reference
     * @param targetName
     */
    public void deleteDataByName(String targetName) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("images");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFound = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    if (targetName.equals(name)) {
                        postSnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("DeleteOperation", "Successfully deleted the record with name: " + targetName);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DeleteOperation", "Failed to delete the record with name: " + targetName, e);
                                });
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    Log.d("DeleteOperation", "No record found with name: " + targetName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DeleteOperation", "Database error: " + databaseError.getMessage());
            }
        });
    }
}
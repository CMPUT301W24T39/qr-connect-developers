package com.example.qrconnect;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

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
    private ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    String fieldName = "eventPoster";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);
        EditText eventTitle = findViewById(R.id.event_title_edittext);
        EditText eventDescriptionEdit = findViewById(R.id.event_description_edit);
        EditText eventDate = findViewById(R.id.event_date);
        EditText eventTime = findViewById(R.id.event_time);
        EditText eventLocation = findViewById(R.id.event_location);
        EditText eventCapacity = findViewById(R.id.event_capacity);
        TextView eventCurrentAttendance = findViewById(R.id.event_current_attendance);
        Switch limitCapacitySwitch = findViewById(R.id.switch_limit_capacity);
        Button shareEventButton = findViewById(R.id.share_event_button);
        Button saveChangesButton = findViewById(R.id.save_event_button);
        Button uploadPosterButton = findViewById(R.id.upload_poster_button);
        ImageView eventPoster = findViewById(R.id.event_image);

        // Initially disable the EditText if you want it to be disabled by default
        eventCapacity.setEnabled(false); // Default state;

        Event currentEvent = (Event) getIntent().getSerializableExtra("EVENT");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(currentEvent.getEventId());
        StorageReference eventPosterRef = storageRef.child("eventposters/" + currentEvent.getEventId() + "_" + fieldName + ".jpg");
        loadEventPoster(eventPosterRef, eventPoster);
        loadEventData(eventRef, eventTitle, eventDescriptionEdit, eventDate, eventTime, eventLocation, eventCapacity, eventCurrentAttendance);



        uploadPosterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                activityResultLauncher.launch(intent);
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            Intent data = result.getData();
                            if (data != null && data.getData() != null) {
                                Uri selectedImageUri = data.getData();
                                eventPoster.setImageURI(selectedImageUri);

                                UploadTask uploadTask = eventPosterRef.putFile(selectedImageUri);
                                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String downloadUrl = uri.toString();

                                                Log.d("Upload", "Image uploaded successfully: " + downloadUrl);
                                            }
                                        });
                                        Toast.makeText(EventDetailsActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EventDetailsActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });

        // Set up a listener for the switch
        limitCapacitySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Switch is on - User can enter capacity
                    eventCapacity.setEnabled(true);
                    eventCapacity.requestFocus(); // Optionally request focus
                } else {
                    // Switch is off - Disable capacity entry and optionally clear or set to "Unlimited"
                    eventCapacity.setEnabled(false);
                    eventCapacity.setText(""); // Clear the text or set to "Unlimited" or similar text
                }
            }
        });

        shareEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent showIntent = new Intent(EventDetailsActivity.this, ShareQRCodeActivity.class);
                showIntent.putExtra("EVENT", currentEvent);
                startActivity(showIntent);

            }
        });

        saveChangesButton.setOnClickListener(v -> {
            // Gather data from UI components
            String title = eventTitle.getText().toString();
            String description = eventDescriptionEdit.getText().toString();
            String date = eventDate.getText().toString();
            String time = eventTime.getText().toString();
            String location = eventLocation.getText().toString();
            int capacity = 0;
            try {
                // Attempt to parse capacity and currentAttendance from EditText inputs
                capacity = Integer.parseInt(eventCapacity.getText().toString());
            } catch (NumberFormatException e) {
                Log.d("EventDetailsActivity", "Capacity is not a valid number, defaulting to 0");
            }

            // Save or update the event in Firestore
            eventRef.update(
                            "title", title,
                            "description", description,
                            "date", date,
                            "time", time,
                            "location", location,
                            "capacity", capacity
                    )
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
        Button viewAttendeeListButton = findViewById(R.id.view_attendee_list_button);

        viewAttendeeListButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    Intent showIntent = new Intent(EventDetailsActivity.this, AttendeeListActivity.class);
                    //TODO: showIntent.putExtra("EVENT", event);
                    showIntent.putExtra("EVENT", currentEvent);
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

        // Event details map locations button
        ImageButton mapLocationsButton = findViewById(R.id.event_details_map_icon_button);
        mapLocationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventDetailsActivity.this, MapLocations.class));
            }
        });


    }
    // Refer from answered Nov 17, 2017 at 13:48 Grimthorr
    //https://stackoverflow.com/questions/47350129/about-the-firestore-query-data-documentation-specifically-documentsnapshot
    @NonNull
    private void loadEventData(DocumentReference eventRef,
                               EditText eventTitle,
                               EditText eventDescriptionEdit,
                               EditText eventDate,
                               EditText eventTime,
                               EditText eventLocation,
                               EditText eventCapacity,
                               TextView eventCurrentAttendance) {
        eventRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Use runOnUiThread to ensure UI updates are on the main thread
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Directly set text on each EditText from the document
                                eventTitle.setText(document.getString("title"));
                                eventDescriptionEdit.setText(document.getString("description"));
                                eventDate.setText(document.getString("date"));
                                eventTime.setText(document.getString("time"));
                                eventLocation.setText(document.getString("location"));
                                try {
                                    Long capacity = document.getLong("capacity");
                                    eventCapacity.setText(capacity != null ? String.valueOf(capacity) : "0");
                                } catch (Exception e) {
                                    eventCapacity.setText("0");
                                    Log.d("EventDetails", "Error loading capacity: " + e.getMessage());
                                }

                                // Handle 'currentAttendance' separately
                                try {
                                    Long currentAttendance = document.getLong("currentAttendance");
                                    eventCurrentAttendance.setText(currentAttendance != null ? String.valueOf(currentAttendance) : "0");
                                } catch (Exception e) {
                                    eventCurrentAttendance.setText("0");
                                    Log.d("EventDetails", "Error loading currentAttendance: " + e.getMessage());
                                }
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EventDetailsActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EventDetailsActivity.this, "Failed to load event", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void loadEventPoster(StorageReference eventPosterRef, ImageView eventPoster) {
        eventPosterRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(EventDetailsActivity.this)
                        .load(uri)
                        .into(eventPoster);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("EventDetailsActivity", "Error loading image: ", exception);
            }
        });
    }


}

package com.example.qrconnect;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EventDetailsInitializeActivity extends AppCompatActivity {
    /**
     * Called when the activity is first created. Responsible for initializing the activity.
     * @param savedInstanceState A Bundle containing the activity's previously frozen state, if there was one.
     */
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef = storage.getReference();
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimeSetListener;
    String fieldName = "eventPoster";

    Event updatedEvent;
    private int year, month, day, hour, minute;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details_initialize_activity);

        EditText eventTitle = findViewById(R.id.init_event_title_edittext);
        EditText eventDescriptionEdit = findViewById(R.id.init_event_description_edit);
        EditText eventDate = findViewById(R.id.init_event_date);
        EditText eventTime = findViewById(R.id.init_event_time);
        EditText eventLocation = findViewById(R.id.init_event_location);
        EditText eventCapacity = findViewById(R.id.init_event_capacity);
        TextView eventCurrentAttendance = findViewById(R.id.init_event_current_attendance);
        Switch limitCapacitySwitch = findViewById(R.id.init_switch_limit_capacity);
        Button nextButton = findViewById(R.id.next_button);
        Button uploadPosterButton = findViewById(R.id.init_upload_poster_button);
        ImageView eventPoster = findViewById(R.id.init_event_image);
        ImageButton calenderButton = findViewById(R.id.init_calender_button);
        ImageButton timeButton = findViewById(R.id.init_time_button);

        // Initially disable the EditText if you want it to be disabled by default
        eventCapacity.setEnabled(false); // Default state;
        eventDate.setFocusable(false);
        eventDate.setFocusableInTouchMode(false);
        eventDate.setOnClickListener(null);

        eventTime.setFocusable(false);
        eventTime.setFocusableInTouchMode(false);
        eventTime.setOnClickListener(null);

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

//        qrCodeGeneratesPage = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                new ActivityResultCallback<ActivityResult>() {
//                    @Override
//                    public void onActivityResult(ActivityResult result) {
//                        if (result.getResultCode() == Activity.RESULT_OK) {
//                            updatedEvent = (Event) result.getData().getSerializableExtra("UPDATED_EVENT");
//                        }
//                    }
//                }
//        );

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
                                        Toast.makeText(EventDetailsInitializeActivity.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EventDetailsInitializeActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        calenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                year = cal.get(Calendar.YEAR);
                month = cal.get(Calendar.MONTH);
                day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        EventDetailsInitializeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                year = selectedYear;
                month = selectedMonth;
                day = selectedDay;
                eventDate.setText(String.format(Locale.getDefault(), "%d/%d/%d", month+1, day, year));
            }
        };

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                hour = cal.get(Calendar.HOUR_OF_DAY);
                minute = cal.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EventDetailsInitializeActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, mTimeSetListener, hour, minute, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();
            }
        });

        mTimeSetListener = new TimePickerDialog.OnTimeSetListener(){
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                eventTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hour, minute));
            }
        };

        nextButton.setOnClickListener(v -> {
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
                    .addOnSuccessListener(aVoid -> Toast.makeText(EventDetailsInitializeActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e -> Toast.makeText(EventDetailsInitializeActivity.this, "Error updating event", Toast.LENGTH_SHORT).show());

            currentEvent.setEventTitle(title);
            currentEvent.setAnnouncement(description);
            currentEvent.setDate(year, month, day);
            currentEvent.setTime(hour, minute);
            currentEvent.setLocation(location);
            currentEvent.setCapacity(capacity);

            Intent intent = new Intent(this, QRCodeGeneratesPage.class);
            intent.putExtra("EVENT", currentEvent);
            startActivity(intent);

        });

        // initialize backButton
        ImageButton backButton = findViewById(R.id.init_event_details_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("events").document(currentEvent.getEventId());

//                if (updatedEvent != null) {
//                    Intent resultIntent = new Intent();
//                    resultIntent.putExtra("UPDATED_EVENT", updatedEvent);
//                    setResult(Activity.RESULT_OK, resultIntent);
//
//                }
                if(currentEvent.getEventCheckInId() == null && currentEvent.getEventPromoId() == null){
                    docRef.delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                                Toast.makeText(getApplicationContext(), "Document successfully deleted!", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("Firestore", "Error deleting document", e);
                                Toast.makeText(getApplicationContext(), "Error deleting document", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                finish();
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
                                Toast.makeText(EventDetailsInitializeActivity.this, "No such document", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(EventDetailsInitializeActivity.this, "Failed to load event", Toast.LENGTH_SHORT).show();
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
                Glide.with(EventDetailsInitializeActivity.this)
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

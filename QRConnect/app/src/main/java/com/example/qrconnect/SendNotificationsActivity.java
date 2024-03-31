package com.example.qrconnect;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * The SendNotificationsActivity class manages the functionality to send notifications.
 * It extends AppCompatActivity.
 */
public class SendNotificationsActivity extends AppCompatActivity {
    private EditText titleEditText;
    private EditText descriptionEditText;
    private FirebaseFirestore db;
    private CollectionReference notificationsRef;

    /**
     * Called when the activity is first created. Responsible for initializing the send notifications.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_notification);

        // Get event from the event details page
        Event event = (Event) getIntent().getSerializableExtra("event");

        // Send notification database initialization with Firebase
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("notifications");

        // Initialize edit text fields for the notification
        titleEditText = findViewById(R.id.send_notification_title);
        descriptionEditText = findViewById(R.id.send_notification_description);

        // Set character limits for the notification text fields
        int title_char_limit = 50;
        int description_char_limit = 250;
        titleEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(title_char_limit)});
        descriptionEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(description_char_limit)});

        // Send notification back button to the event details page
        ImageButton backButton = findViewById(R.id.notifications_page_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Send notification button to send a notification to the attendees in the event
        Button sendButton = findViewById(R.id.send_notification_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification(event);
            }
        });
    }

    /**
     * Send notifications to the Firestore Database
     */
    private void sendNotification(Event event) {
        // Get title field from the event object
        String eventTitle = event.getEventTitle();

        // Convert title and description edit text fields to strings
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Get the date and time when the notification is sent
        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
        }
        LocalDateTime date = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            date = LocalDateTime.now();
        }
        String date_string = "N/A";
        if (date != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                date_string = dtf.format(date);
            }
        }

        // Default read to false (unread)
        boolean read = false;

        // Able to send a notification without a description
        if (!title.isEmpty()) {
            // Create a new notification object
            Notification notification = new Notification(eventTitle, title, description, date_string, read);

            // Add the notification to Firestore
            notificationsRef.add(notification)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(SendNotificationsActivity.this, "Notification sent successfully.", Toast.LENGTH_SHORT).show();
                            // Clear the title and description EditText fields after sending the notification
                            titleEditText.setText("");
                            descriptionEditText.setText("");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SendNotificationsActivity.this, "Failed to send notification.", Toast.LENGTH_SHORT).show();
                            Log.e("Firestore", "Error adding notification.", e);
                        }
                    });
        }
        else {
            // If nothing entered and tried to send a notification
            Toast.makeText(SendNotificationsActivity.this, "Please enter a title and a description.", Toast.LENGTH_SHORT).show();
        }
    };
}
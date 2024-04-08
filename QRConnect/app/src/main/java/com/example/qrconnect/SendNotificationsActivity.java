package com.example.qrconnect;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import androidx.core.app.NotificationCompat;
import androidx.core.os.BuildCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private CollectionReference usersRef;
    private String userId;

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

        // Get user ID from SharedPreferences
        userId = UserPreferences.getUserId(this);

        // Get event from the event details page
        Event event = (Event) getIntent().getSerializableExtra("event");

        // Send notification database initialization with Firebase
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("users").document(userId).collection("notifications");
        usersRef = db.collection("users");

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
        String eventId = event.getEventId();
        String eventHostId = event.getHostId();

        // Get title field from the event object
        String eventTitle = event.getEventTitle();

        // Convert title and description edit text fields to strings
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();

        // Get the date and time when the notification is sent
        DateTimeFormatter dtf = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
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
            Notification notification = new Notification(eventTitle, title, description, date_string, read, eventId);
            // Checks if there are any attendees to send the notitication to
            if (!event.getAttendeeListIdToName().isEmpty()) {
                makePushNotification(title, description);
                for (String attendeeId : event.getAttendeeListIdToName().keySet()) {
                    usersRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot userDocument : task.getResult()) {
                                    String userId = userDocument.getId();
                                    CollectionReference userNotifications = usersRef.document(userId).collection("notifications");
                                    if (userId.equals(attendeeId)) {
                                        // Add the notification to Firestore
                                        userNotifications.add(notification)
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
                                }
                            } else {
                                Log.d("Firestore", "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
            }
            else {
                // If no attendee to send notification to
                Toast.makeText(SendNotificationsActivity.this, "No attendees to send the notification to.", Toast.LENGTH_SHORT).show();
            }
        }
        else if (title.isEmpty() && description.isEmpty()) {
            // If nothing entered and tried to send a notification
            Toast.makeText(SendNotificationsActivity.this, "Please enter a title and a description.", Toast.LENGTH_SHORT).show();
        }
        else if (title.isEmpty()) {
            // If nothing entered and tried to send a notification
            Toast.makeText(SendNotificationsActivity.this, "Please enter a title.", Toast.LENGTH_SHORT).show();
        }
        else {
           // If unable to send notification for other reason
           Toast.makeText(SendNotificationsActivity.this, "Unable to send notification.", Toast.LENGTH_SHORT).show();
        }
    };

    // Referenced https://www.youtube.com/watch?v=vyt20Gg2Ckg&ab_channel=CodesEasy for the push notification implementation
    /**
     * Creates a push notification.
     * @param title the title of the notification.
     * @param description the description of the notification.
     */
    public void makePushNotification(String title, String description){
        String chanelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), chanelID);
        builder.setSmallIcon(R.drawable.push_notification)
                .setContentTitle(title)
                .setContentText(description)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getApplicationContext(), PushNotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("data", "Some value to be passed here");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_MUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (BuildCompat.isAtLeastQ()) {
            NotificationChannel notificationChannel =
                    notificationManager.getNotificationChannel(chanelID);
            if (notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(chanelID, "Some description", importance);
                notificationChannel.setLightColor(Color.GREEN);
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        notificationManager.notify(0,builder.build());
    }
}

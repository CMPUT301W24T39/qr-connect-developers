package com.example.qrconnect;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.CountDownLatch;

/**
 * The MilestoneManager class manages the event milestone notifications.
 */
public class MilestoneManager {
    private static Integer capacity;
    private FirebaseFirestore db;
    private CollectionReference notificationsRef;
    private CollectionReference eventsRef;
    private MilestoneListener milestoneListener;
    private MainActivity activity;
    boolean[] mileStoneReached;
    private String userId;
    private ArrayList<Notification> notificationsDataList;

    /**
     * MilestoneManager constructor.
     */
    public MilestoneManager(MainActivity mainActivity, CollectionReference notifications, String id, ArrayList<Notification> notificationsList){
        activity = mainActivity;
        notificationsRef = notifications;
        userId = id;

        mileStoneReached = new boolean[1];
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        notificationsDataList = notificationsList;
        Log.d("MilestoneManager", "Notification data list: " + notificationsDataList);
    }

    public void startManager() {
        // Start the notification listener to check notifications in real time and update the UI accordingly
        milestoneListener = new MilestoneListener(this, userId);
        Log.d("MilestoneManager", "Milestone manager for user id: " + userId);
        milestoneListener.startListening();
    }

    /**
     * Checks if any milestones have been hit for an event.
     * @param eventTitle the title of the event.
     * @param capacity the capacity of the event.
     * @param currentAttendance the current attendance of the event.
     */
    public void checkMilestones(String eventId, String eventTitle, Integer capacity, Integer currentAttendance) {
        List<Integer> milestones = Arrays.asList(3, 5, 10, 25, 50, 100);

        // Log milestone check start
        Log.d("MilestoneManager", "Checking milestones for event: " + eventTitle);
        Log.d("MilestoneManager", "Notification data list: " + notificationsDataList);


        // First person milestone
        if (currentAttendance == 1) {
            String title = "Event Milestone Reached!";
            String description = "Congratulations! Your event has its first attendee!";
            checkIfMilestoneAlreadyReached(description, eventId);
            // Check if milestone was reached
            if (!mileStoneReached[0]) {
                sendNotification(eventId, eventTitle, title, description);
            }
        }
        // Checks if the current attendance is at the capacity
        else if (currentAttendance.equals(capacity) && capacity != 0) {
            String title = "Event Milestone Reached!";
            String description = "Congratulations! Your event has reached its capacity of " + capacity + "!";
            checkIfMilestoneAlreadyReached(description, eventId);
            // Check if milestone was reached
            if (!mileStoneReached[0]) {
                sendNotification(eventId, eventTitle, title, description);
            }
        }
        // Check milestones
        else {
            for (Integer milestone : milestones) {
                // Check if current attendance equals the milestone
                if (currentAttendance.equals(milestone)) {
                    String title = "Event Milestone Reached!";
                    String description = "Congratulations! Your event has reached " + milestone + " attendees!";
                    checkIfMilestoneAlreadyReached(description, eventId);
                    // Check if milestone was reached
                    if (!mileStoneReached[0]) {
                        sendNotification(eventId, eventTitle, title, description);
                    }

                }
            }
        }
    }

    /**
     * Send milestone notifications to the Firestore Database.
     */
    private void sendNotification(String eventId, String eventTitle, String title, String description) {

        // Log notification sending
        Log.d("MilestoneManager", "Sending notification for event: " + eventTitle);

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

        // Type: Push Notification
        String type = "milestone";

        // Create a new notification object
        Notification notification = new Notification(eventTitle, title, description, date_string, read, eventId, type);
        // Add the notification to Firestore
        notificationsRef.add(notification)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.e("Firestore", "Notification added successfully.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firestore", "Error adding notification.", e);
                    }
                });
    }

    /**
     * Check if the milestone of the event was already reached.
     */
    private void checkIfMilestoneAlreadyReached(String description, String eventId) {

        // Log check old milestones
        Log.d("MilestoneManager", "Check if milestone happened for event: " + eventId);

        mileStoneReached[0] = false; // Default to false (milestone hasn't already been reached)

        for (Notification notification : notificationsDataList) {
            String documentEventId = notification.getNotificationEventId();
            Log.d("MilestoneManager", "Check the event milestone for event1: " + documentEventId);
            Log.d("MilestoneManager", "Check the event milestone for event2: " + eventId);
            String documentDescription = notification.getNotificationDescription();
            Log.d("MilestoneManager", "Check the description milestone for event1: " + documentDescription);
            Log.d("MilestoneManager", "Check the description milestone for event2: " + description);
            if (documentEventId != null) {
                Log.d("MilestoneManager", "Condition 1: documentEventId is not null");
            }
            if (documentEventId.equals(eventId)) {
                Log.d("MilestoneManager", "Condition 2: documentEventId equals eventId");
            }
            if (documentDescription != null) {
                Log.d("MilestoneManager", "Condition 3: documentDescription is not null");
            }
            if (documentDescription.equals(description)) {
                Log.d("MilestoneManager", "Condition 4: documentDescription equals description");
            }
            if (documentEventId != null && documentEventId.equals(eventId) && documentDescription != null && documentDescription.equals(description)){
                // If the milestone was already reached for this event
                mileStoneReached[0] = true;
                Log.d("MilestoneManager", "Milestone reached: " + mileStoneReached[0]);
                break;
            }
        }

        /*notificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot userDocument : task.getResult()) {
                    // Event Id
                    String documentEventId = userDocument.getString("notificationEventId");
                    Log.d("MilestoneManager", "Check the event milestone for event1: " + documentEventId);
                    Log.d("MilestoneManager", "Check the event milestone for event2: " + eventId);
                    // Event Description
                    String documentDescription = userDocument.getString("notificationDescription");
                    Log.d("MilestoneManager", "Check the description milestone for event1: " + documentDescription);
                    Log.d("MilestoneManager", "Check the description milestone for event2: " + description);

                    if (documentEventId != null) {
                        Log.d("MilestoneManager", "Condition 1: documentEventId is not null");
                    }
                    if (documentEventId.equals(eventId)) {
                        Log.d("MilestoneManager", "Condition 2: documentEventId equals eventId");
                    }
                    if (documentDescription != null) {
                        Log.d("MilestoneManager", "Condition 3: documentDescription is not null");
                    }
                    if (documentDescription.equals(description)) {
                        Log.d("MilestoneManager", "Condition 4: documentDescription equals description");
                    }
                    if (documentEventId != null && documentEventId.equals(eventId) && documentDescription != null && documentDescription.equals(description)){
                        // If the milestone was already reached for this event
                        mileStoneReached[0] = true;
                        Log.d("MilestoneManager", "Milestone reached: " + mileStoneReached[0]);
                        break;
                    }
                }
            } else {
                Log.d("Firestore", "Error getting documents: ", task.getException());
            }
        });*/
    }
}
package com.example.qrconnect;

import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/**
 * The MilestoneManager class manages the event milestone notifications.
 */
public class MilestoneManager {
    private static Integer capacity;
    private FirebaseFirestore db;
    private CollectionReference notificationsRef;
    private MilestoneListener milestoneListener;
    private MainActivity activity;

    /**
     * MilestoneManager constructor.
     */
    public MilestoneManager(MainActivity mainActivity){
        activity = mainActivity;
        // Send notification database initialization with Firebase
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("notifications");
    }

    public void startManager() {
        // Start the notification listener to check notifications in real time and update the UI accordingly
        milestoneListener = new MilestoneListener(this);
        milestoneListener.startListening();
    }

    /**
     * Checks if any milestones have been hit for an event.
     * @param eventTitle the title of the event.
     * @param capacity the capacity of the event.
     * @param currentAttendance the current attendance of the event.
     */
    public void checkMilestones(String eventTitle, Integer capacity, Integer currentAttendance) {
        List<Integer> milestones = Arrays.asList(10, 25, 50, 100, 500);

        // First person milestone
        if (currentAttendance == 1) {
            String title = "Event Milestone Reached!";
            String description = "Congratulations! Your event has its first attendee!";
            sendNotification(eventTitle, title, description);
        }

        // Check milestones
        for (Integer milestone : milestones) {
            // Check if current attendance equals the milestone
            if (currentAttendance == milestone) {
                String title = "Event Milestone Reached!";
                String description = "Congratulations! Your event has reached " + milestone + " attendees!";
                sendNotification(eventTitle, title, description);
            }
        }
        // Checks if the current attendance is at the capacity
        if (currentAttendance == capacity && capacity != 0) {
            String title = "Event Milestone Reached!";
            String description = "Congratulations! Your event has reached its capacity of " + capacity + "!";
            sendNotification(eventTitle, title, description);
        }
    }

    /**
     * Send milestone notifications to the Firestore Database.
     */
    private void sendNotification(String eventTitle, String title, String description) {

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

        // Create a new notification object
        Notification notification = new Notification(eventTitle, title, description, date_string, read);
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
}
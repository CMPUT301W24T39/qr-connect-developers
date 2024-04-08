package com.example.qrconnect;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import android.os.Handler;

import java.util.Map;

/**
 * The MilestoneListener class is responsible for checking the event database in real time.
 * Calls the sendNotifications() in MilestoneManager to send milestone notifications based on current attendance of an event.
 */
public class MilestoneListener {
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private ListenerRegistration listenerRegistration;
    private MilestoneManager activity;
    private String userId;
    private boolean isCheckingMilestones = false;
    private Handler debounceHandler;

    /**
     * MilestoneListener constructor.
     * Get MilestoneManager instance.
     * @param milestoneManager the MainActivity instance.
     */
    public MilestoneListener(MilestoneManager milestoneManager, String id) {
        activity = milestoneManager;
        userId = id;

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
    }

    /**
     * Start the event listener.
     */
    public void startListening() {
        Log.d("MilestoneListener", "Starting event milestone listener...");
        eventsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            // If there are any errors while listening
            if (e != null) {
                Log.e("TAG", "Error listening to events", e);
                return;
            }
            // Debounce by waiting for a brief period after the last modification before taking action
            debounce(() -> {
                // Goes through the events database
                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case MODIFIED: // If an event was modified
                            DocumentSnapshot newDocumentSnapshot = dc.getDocument();

                            DocumentSnapshot oldDocumentSnapshot = dc.getDocument();

                            // Host Id
                            String hostId = newDocumentSnapshot.getString("hostId");
                            Log.d("Firestore", "Milestone Host ID: " + hostId);
                            Log.d("Firestore", "Milestone User ID: " + userId);
                            if (hostId != null && hostId.equals(userId)) {
                                // Event Id
                                String eventId = newDocumentSnapshot.getString("eventId");
                                // Event title
                                String title = newDocumentSnapshot.getString("title");
                                // Event capacity
                                Integer capacity = 0;
                                if (newDocumentSnapshot.getLong("capacity") != null) {
                                    capacity = newDocumentSnapshot.getLong("capacity").intValue();
                                }
                                // Event current attendance
                                Integer currentAttendance = newDocumentSnapshot.getLong("currentAttendance").intValue();

                                // Check if milestones are already being checked
                                if (!isCheckingMilestones) {
                                    // Set the flag to true before checking milestones
                                    isCheckingMilestones = true;
                                    // Check milestones related to capacity and currentAttendance
                                    activity.checkMilestones(eventId, title, capacity, currentAttendance);
                                    // Reset the flag after checking milestones
                                    isCheckingMilestones = false;
                                }
                            }
                    }
                    break;
                }
            }, 500);
        });
    }

    /**
     * Stop the milestone listener.
     */
    public void stopListening() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }

    // Referenced https://medium.com/vena-engineering/how-to-building-a-debouncer-in-java-987e605e2be6 for implementation of debounce function
    /**
     * Debouncing mechanism to wait for a brief period of time after the last modification before taking action.
     * When scanning a QR Code to check in, prevents multiple accesses to the milestone check functions.
     * @param action the action to wait for.
     * @param delayMillis the period of time to wait after the last modification.
     */
    private void debounce(Runnable action, long delayMillis) {
        // Cancel any pending debounced action
        if (debounceHandler != null) {
            debounceHandler.removeCallbacksAndMessages(null);
        }
        // Schedule the action to be executed after the delay
        debounceHandler = new Handler(Looper.getMainLooper());
        debounceHandler.postDelayed(action, delayMillis);
    }
}
package com.example.qrconnect;
import android.util.Log;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

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
        listenerRegistration = eventsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            // If there are any errors while listening
            if (e != null) {
                Log.e("TAG", "Error listening to events", e);
                return;
            }
            // Goes through the events database
            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case MODIFIED: // If an event was modified
                        DocumentSnapshot newDocumentSnapshot = dc.getDocument();
                        // Host Id
                        String hostId = newDocumentSnapshot.getString("hostId");
                        Log.d("Firestore", "Host ID: " + hostId);
                        Log.d("Firestore", "User ID: " + userId);
                        if (hostId != null && hostId.equals(userId)) {
                            // Event Id
                            String eventId = newDocumentSnapshot.getString("eventId");
                            // Event title
                            String title = newDocumentSnapshot.getString("title");
                            // Event capacity
                            Integer capacity = newDocumentSnapshot.getLong("capacity").intValue();
                            // Event current attendance
                            Integer currentAttendance = newDocumentSnapshot.getLong("currentAttendance").intValue();

                            // Check milestones related to capacity and currentAttendance
                            activity.checkMilestones(eventId, title, capacity, currentAttendance);
                        }
                        break;
                }
            }
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
}
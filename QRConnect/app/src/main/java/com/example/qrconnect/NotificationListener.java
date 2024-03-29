package com.example.qrconnect;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

/**
 * The NotificationListener class is responsible for checking the notifications database in real time.
 * Calls the checkNotifications() in MainActivity to update the notification bell if a notification was added or modified.
 */
public class NotificationListener {
    private FirebaseFirestore db;
    private CollectionReference notificationsRef;
    private ListenerRegistration listenerRegistration;
    private MainActivity activity;

    /**
     * NotificationListener constructor.
     * Get MainActivity instance.
     * @param mainActivity the MainActivity instance.
     */
    public NotificationListener(MainActivity mainActivity) {
        activity = mainActivity;
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("notifications");
    }

    /**
     * Start the notification listener.
     */
    public void startListening() {
        listenerRegistration = notificationsRef.addSnapshotListener((queryDocumentSnapshots, e) -> {
            // If there are any errors while listening
            if (e != null) {
                return;
            }
            // Goes through the notifications database
            for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                switch (dc.getType()) {
                    case ADDED: // If a notification was added
                        activity.checkNotifications();
                        break;
                    case MODIFIED: // If a notification was modified (unread -> read)
                        activity.checkNotifications();
                        break;
                }
            }
        });
    }

    /**
     * Stop the notification listener.
     */
    public void stopListening() {
        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }
    }
}
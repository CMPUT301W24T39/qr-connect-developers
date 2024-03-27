package com.example.qrconnect;

import com.google.firebase.firestore.FirebaseFirestore;

/**
 * The MilestoneManager class manages the event milestone notifications.
 */
public class MilestoneManager {
    private static Integer capacity;
    private FirebaseFirestore db;

    public static void checkMilestones(Event event) {
        capacity = event.getCapacity();
        // TODO

    }

    private static void sendNotification(String message) {
       // TODO
    }
}
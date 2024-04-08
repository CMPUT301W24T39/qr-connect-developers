package com.example.qrconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The AttendeeNotifications class manages the attendee notifications page.
 * It extends AppCompatActivity.
 */
public class AttendeeNotifications extends AppCompatActivity {

    ListView notificationsList;
    ArrayList<Notification> notificationsDataList;
    NotificationArrayAdapter notificationArrayAdapter;
    private FirebaseFirestore db;
    private CollectionReference notificationsRef;
    private String userId;

    /**
     * Called when the activity is first created. Responsible for initializing the notifications page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_notifications);

        // Get user ID from SharedPreferences
        userId = UserPreferences.getUserId(this);

        // Notification back button to the user home screen
        ImageButton backButton = findViewById(R.id.attendee_notifications_page_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Notification database initialization with Firebase
        db = FirebaseFirestore.getInstance();
        notificationsRef = db.collection("users").document(userId).collection("notifications");

        Intent intent = getIntent();
        notificationsList = findViewById(R.id.notifications_list);
        notificationsDataList = NotificationDataListManager.getInstance().getNotificationsDataList();
        notificationArrayAdapter = new NotificationArrayAdapter(this, notificationsDataList);
        notificationsList.setAdapter(notificationArrayAdapter);

        receiveNotifications();
    }

    /**
     * Receive notifications from Firestore Database and update the notifications ListView
     */
    private void receiveNotifications() {
        notificationsDataList.clear();
        notificationsRef.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String event = documentSnapshot.getString("notificationEvent");
                        String title = documentSnapshot.getString("notificationTitle");
                        String description = documentSnapshot.getString("notificationDescription");
                        String date = documentSnapshot.getString("notificationDate");
                        boolean read = documentSnapshot.getBoolean("notificationRead");
                        String eventId = documentSnapshot.getString("notificationEventId");
                        String type = documentSnapshot.getString("notificationType");
                        Notification notification = new Notification(event, title, description, date, read, eventId, type);
                        notificationsDataList.add(notification);
                    }
                    sortNotifications();
                }
            });
    }

    /**
     * Sort notificationsDataList by date so most recent is at the top.
     */
    private void sortNotifications(){
        Collections.sort(notificationsDataList, new Comparator<Notification>() {
            @Override
            public int compare(Notification n1, Notification n2) {
                return n2.getNotificationDate().compareTo(n1.getNotificationDate());
            }
        });
        notificationArrayAdapter.notifyDataSetChanged();
    }
}
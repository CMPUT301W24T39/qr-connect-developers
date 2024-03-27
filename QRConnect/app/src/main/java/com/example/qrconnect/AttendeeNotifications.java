package com.example.qrconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

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
        notificationsRef = db.collection("notifications");

        notificationsList = findViewById(R.id.notifications_list);
        notificationsDataList = new ArrayList<>();
        notificationArrayAdapter = new NotificationArrayAdapter(this, notificationsDataList);
        notificationsList.setAdapter(notificationArrayAdapter);
        receiveNotifications();

        // Set up item click listener for the notification ListView
        notificationsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    /**
     * Receive notifications from Firestore Database and update the notifications ListView
     */
    private void receiveNotifications() {
        notificationsRef.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        String event = documentSnapshot.getString("notificationEvent");
                        String title = documentSnapshot.getString("notificationTitle");
                        String description = documentSnapshot.getString("notificationDescription");
                        String date = documentSnapshot.getString("notificationDate");
                        Notification notification = new Notification(event, title, description, date);
                        notificationsDataList.add(notification);
                    }
                    notificationArrayAdapter.notifyDataSetChanged();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Firestore", "Error receiving notifications: " + e.getMessage());
                }
            });
    }
}
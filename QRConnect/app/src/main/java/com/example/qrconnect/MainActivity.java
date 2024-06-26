package com.example.qrconnect;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.os.BuildCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;
import java.text.SimpleDateFormat;
/*
https://developer.android.com/media/camera/camera-deprecated/photobasics
https://stackoverflow.com/questions/5089300/how-can-i-change-the-image-of-an-imageview
https://stackoverflow.com/questions/71729415/saving-an-image-and-displaying-it-in-gallery
https://www.geeksforgeeks.org/how-to-update-data-in-firebase-firestore-in-android/
https://stackoverflow.com/questions/41296416/updating-the-data-on-firebase-android
https://www.geeksforgeeks.org/how-to-update-data-in-firebase-firestore-in-android/
https://www.jsowl.com/get-a-document-using-its-id-from-a-collection-in-firestore/#google_vignette
https://developer.android.com/training/articles/user-data-ids
https://stackoverflow.com/questions/52860840/retrieving-document-id-from-firestore-collection-android
https://stackoverflow.com/questions/2550099/how-to-kill-an-android-activity-when-leaving-it-so-that-it-cannot-be-accessed-fr
https://developer.android.com/guide/components/activities/activity-lifecycle
https://stackoverflow.com/questions/32306458/recreate-restart-activity-from-another-activity
https://developer.android.com/guide/components/activities/tasks-and-back-stack
https://stackoverflow.com/questions/3473168/clear-the-entire-history-stack-and-start-a-new-activity-on-android
https://stackoverflow.com/questions/2091465/how-do-i-pass-data-between-activities-in-android-application
https://stackoverflow.com/questions/45410314/save-class-to-firebase
https://stackoverflow.com/questions/57134070/error-inflating-class-com-google-android-material-textfield-textinputlayout
https://www.youtube.com/watch?v=KsprqXfugGQ
https://www.geeksforgeeks.org/handling-click-events-button-android/
https://www.youtube.com/watch?v=MCeWm8qu0sw
https://stackoverflow.com/questions/44676579/how-to-make-option-menu-appear-on-bottom-of-the-screen
https://stackoverflow.com/questions/72713837/redirecting-user-to-menu-section-after-button-click-by-popping-up-nav-bar-from
https://developer.android.com/training/basics/intents/sending
https://www.youtube.com/watch?v=hwe1abDO2Ag
https://www.youtube.com/watch?v=c6c1giRekB4
https://www.youtube.com/watch?v=KO8tSyTmV24
 */

/**
 * The MainActivity class maintains the functions of the main activity.
 * It extends AppCompatActivity.
 */
public class MainActivity extends AppCompatActivity{
    private FloatingActionButton addButton;
    private ImageButton profileButton;
    private ImageButton notificationButton;
    private ImageButton browseEventsButton;
    static ArrayList<Event> eventDataList = new ArrayList<Event>();
    static ArrayList<Event> userEventDataList = new ArrayList<Event>();
    ListView eventList;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private static CollectionReference userNotificationsRef;
    private ActivityResultLauncher<Intent> eventDetailsInitializeActivity;
    private EventAdapter eventAdapter;
    private NotificationListener notificationListener;
    private MilestoneManager milestoneManager;
    private String userId;
    private ArrayList<Notification> notificationsDataList;

    /**
     * This defines the functions in main activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get user ID from SharedPreferences
        userId = UserPreferences.getUserId(this);

        // Initialize database
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");
        userNotificationsRef = db.collection("users").document(userId).collection("notifications");

        // Start the notification listener to check notifications in real time and update the UI accordingly
        notificationListener = new NotificationListener(this, userNotificationsRef);
        notificationListener.startListening();
        notificationsDataList = NotificationDataListManager.getInstance().getNotificationsDataList();

        // Initialize milestone manager
        milestoneManager= new MilestoneManager(this, userNotificationsRef, userId, notificationsDataList);
        milestoneManager.startManager();

        // Initialize buttons
        eventList = findViewById(R.id.event_list_list);
        addButton = findViewById(R.id.button_add_event);
        profileButton = findViewById(R.id.user_icon_button);
        notificationButton = findViewById(R.id.notification_icon_button);
        browseEventsButton = findViewById(R.id.explore_event_button);

        // Initialize adapters
        eventAdapter = new EventAdapter(this, userEventDataList);
        eventList.setAdapter(eventAdapter);

        // Referenced https://www.youtube.com/watch?v=vyt20Gg2Ckg&ab_channel=CodesEasy for the push notification implementation
        // Push notification permissions (asks users if QRConnect is allowed to send notifications to their device)
        FirebaseApp.initializeApp(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Add event button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event newEvent = new Event();
                String uniqueID = UUID.randomUUID().toString();
                newEvent.setEventTitle("New Event " + (userEventDataList.size() +1));
                newEvent.setEventId(uniqueID);
                String hostId = UserPreferences.getUserId(getApplicationContext());
                newEvent.setHostId(hostId);
                eventAdapter.notifyDataSetChanged();
                addNewEvent(newEvent);
                Intent intent = new Intent(MainActivity.this, EventDetailsInitializeActivity.class);
                intent.putExtra("EVENT", newEvent);
                startActivity(intent);
            }
        });

        // Getting events from Firebase
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    eventDataList.clear();
                    userEventDataList.clear();
                    // globalEventDataList.clear();
                    for (QueryDocumentSnapshot doc: querySnapshots){
                        String eventId = doc.getId();
                        String eventTitle = doc.getString("title");
                        String eventTimeString = doc.getString("time");
                        Calendar eventTime = null;
                        if (eventTimeString != null && !eventTimeString.isEmpty()) {
                            eventTime = TimeConverter.stringToCalendar(eventTimeString);
                        } else {
                            Log.e("Firestore", "Event time is null or empty for document: " + doc.getId());
                        }
                        String eventDateString = doc.getString("date");
                        Calendar eventDate = null;
                        if (eventDateString != null && !eventDateString.isEmpty()) {
                            eventDate = DateConverter.stringToCalendar(eventDateString);
                        } else {
                            Log.e("Firestore", "Event time is null or empty for document: " + doc.getId());
                        }
                        String eventLocation = doc.getString("location");
//                        if (doc.getString("capacity") != null){
//                            Integer eventCapacity = Integer.parseInt(doc.getString("capacity"));}
                        String eventDescription = doc.getString("description");
                        String checkInId = doc.getString("checkInQRCodeImageUrl");
                        String promoId = doc.getString("promoQRCodeImageUrl");
                        String hostId = doc.getString("hostId");
                        HashMap<String, Long> attendeeListIdToTimes = (HashMap<String, Long>) doc.get("attendeeListIdToTimes");
                        HashMap<String, String> attendeeListIdToName = (HashMap<String, String>) doc.get("attendeeListIdToName");
                        HashMap<String, String> attendeeListIdToLocation = (HashMap<String, String>) doc.get("attendeeListIdToLocation");
                        HashMap<String, String> signupUserIdToName = (HashMap<String, String>) doc.get("signupUserIdToName");
                        eventDataList.add(new Event(eventTitle, eventDate, eventTime,
                                eventLocation, 0, eventDescription, checkInId, promoId, eventId,
                                hostId, attendeeListIdToTimes, attendeeListIdToName, attendeeListIdToLocation, signupUserIdToName));

                        Log.d("Firestore", String.format("Event(%s %s %s %s %s %s %s %s %s) fetched", eventTitle, eventDate, eventTime, eventLocation, 0, eventDescription, checkInId, promoId, eventId));
                    }
                    getUserEvents();
                    eventAdapter.notifyDataSetChanged();
                }
            }
        });

        // User homepage profile button
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserProfilePage.class));
            }
        });

        // User homepage browse events button
        browseEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendeeBrowseEvents.class);
                intent.putExtra("events", (Serializable) eventDataList);
                startActivity(intent);
            }
        });

        // User homepage attendee notifications button
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AttendeeNotifications.class);
                startActivity(intent);
                // When this activity is launched, mark all the notifications as read
                markNotificationsAsRead();
            }
        });

        // Event list clicker
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Event currentEvent = eventAdapter.getItem(position);
                    String userId = UserPreferences.getUserId(getApplicationContext());
                    String hostId = currentEvent.getHostId();
                    Intent showIntent;
                    if (userId.equals(hostId)){
                        showIntent = new Intent(MainActivity.this, EventDetailsActivity.class);
                        showIntent.putExtra("EVENT", currentEvent);
                    }
                    else{
                        showIntent = new Intent(MainActivity.this, PromoDetailsActivity.class);
                        showIntent.putExtra("EVENT_ID", currentEvent.getEventId());
                    }
                    startActivity(showIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // User homepage scan qr code button
        Button cameraButton = findViewById(R.id.qr_code_scanner_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MainActivity.this, QRCodeCheckInActivity.class));
                startActivity(new Intent(MainActivity.this, BarcodeScanningActivity.class));
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        eventAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("UPDATED_EVENT")) {
            Event updatedEvent = (Event) intent.getSerializableExtra("UPDATED_EVENT");

            eventDataList.add(updatedEvent);
            // globalEventDataList.add(updatedEvent);
            addNewEvent(updatedEvent);
            eventAdapter.notifyDataSetChanged();
        }
    }
    /**
     * This adds a new event to firebase.
     * @param event This is a event that is added to firebase.
     */
    private void addNewEvent(Event event) {

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        String formattedTime = "";
        if (event.getTime() != null) {
            formattedTime = timeFormat.format(event.getTime().getTime());
        } else {
            Log.e("Firestore", "Event time is null for event: " + event.getEventTitle());
            formattedTime = timeFormat.format(Calendar.getInstance().getTime());
        }

        String formattedDate = "";
        if (event.getDate() != null) {
            formattedDate = dateFormat.format(event.getDate().getTime());
        } else {
            Log.e("Firestore", "Event time is null for event: " + event.getEventTitle());
            formattedDate = dateFormat.format(Calendar.getInstance().getTime());
        }


        HashMap<String, Object> data = new HashMap<>();
        data.put("title", event.getEventTitle());
        data.put("date", formattedDate);
        data.put("time", formattedTime);
        data.put("location", event.getLocation());
        data.put("capacity", event.getCapacity());
        data.put("description", event.getDescription());
        data.put("eventId", event.getEventId());
        data.put("checkInQRCodeImageUrl", event.getEventCheckInId());
        data.put("promoQRCodeImageUrl", event.getEventPromoId());
        data.put("posterURL", event.getEventPosterUrl());
        data.put("hostId", event.getHostId());
        data.put("attendeeListIdToTimes", event.getAttendeeListIdToCheckInTimes());
        data.put("attendeeListIdToName", event.getAttendeeListIdToName());
        data.put("attendeeListIdToLocation", event.getAttendeeListIdToLocation());
        data.put("signupUserIdToName", event.getSignupUserIdToName());
        data.put("currentAttendance", 0L);
        eventsRef
                .document(event.getEventId())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                });
    }


    /**
     * Checks if the notifications are read or unread.
     * If all notifications are read, make the alert on the notification bell invisible.
     * If not all the notifications are read, make the alert on the notification bell visible.
     */
    public void checkNotifications() {
        Log.d("PUSHNOTIFICATION", "Check notification");
        userNotificationsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean allRead = true;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    boolean notificationRead = document.getBoolean("notificationRead");
                    if (!notificationRead) {
                        allRead = false;
                        break;
                    }
                }
                // If all the notifications are read (notificationRead == true) then change alert to invisible
                if (allRead) {
                    findViewById(R.id.notification_alert).setVisibility(View.INVISIBLE);
                }
                // If not all the notifications are read then change alert to visible
                else {
                    findViewById(R.id.notification_alert).setVisibility(View.VISIBLE);
                }
            }
        });
    }

    /**
     * When the attendee notification page is opened, marks all notifications as read.
     * Changes the boolean notificationRead to true for all notifications in the database.
     */
    private void markNotificationsAsRead() {
        // For notificationRead that are false
        userNotificationsRef.whereEqualTo("notificationRead", false)
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Change notificationRead to true
                            userNotificationsRef.document(document.getId()).update("notificationRead", true);
                        }
                    }
                }
            });
    }

    /**
     * Stops the notification listener.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (notificationListener != null) {
            notificationListener.stopListening();
        }
    }

    /**
     * Goes through the events collection and separates the events unique to the user.
     * These events include where the user id matches the host id (organizer),
     * where the user id is in the attendeelist of an event (attendee/checked in),
     * or where the user id is in the signuplist of an event (signed up).
     */
    private void getUserEvents() {
        for (Event event : eventDataList) {
            if (event.getHostId() != null && event.getHostId().equals(userId) || event.getAttendeeListIdToName().containsKey(userId)
                || event.getSignupUserIdToName().containsKey(userId)) {
                userEventDataList.add(event);
            }
        }
    }

    /**
     * Checks if a notification is of type push so it can be sent as a push notification.
     * @param addedDocument the notification that was added to the firebase.
     */
    public void checkPushNotification(DocumentSnapshot addedDocument) {
        Log.d("PUSHNOTIFICATION", "Check push notification");
        String type = addedDocument.getString("notificationType");
        Log.d("PUSHNOTIFICATION", "Notification type: " + type);
        String title = addedDocument.getString("notificationTitle");
        String description = addedDocument.getString("notificationDescription");
        if (type != null && type.equals("push")) {
            makePushNotification(title, description);
        }

    }

    // Referenced https://www.youtube.com/watch?v=vyt20Gg2Ckg&ab_channel=CodesEasy for the push notification implementation
    /**
     * Creates a push notification.
     * @param title the title of the push notification.
     * @param description the description of the push notification.
     */
    public void makePushNotification(String title, String description){
        Log.d("PUSHNOTIFICATION", "Make push notification");
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
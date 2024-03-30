package com.example.qrconnect;

import static com.example.qrconnect.TimeConverter.stringToCalendar;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
 */

/**
 * The MainActivity class maintains the functions of the main activity.
 * It extends AppCompatActivity.
 */
public class MainActivity extends AppCompatActivity implements DeleteEventFragment.DeleteEventDialogListener{
    private FloatingActionButton addButton;
    private ImageButton profileButton;
    private ImageButton notificationButton;
    private ImageButton browseEventsButton;
    static ArrayList<Event> eventDataList = new ArrayList<Event>();
    ListView eventList;
    static boolean isAddButtonClicked = false;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;

    private ActivityResultLauncher<Intent> eventDetailsInitializeActivity;
    private EventAdapter eventAdapter;

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

        eventList = findViewById(R.id.event_list_list);
        addButton = findViewById(R.id.button_add_event);
        profileButton = findViewById(R.id.user_icon_button);
        notificationButton = findViewById(R.id.notification_icon_button);
        browseEventsButton = findViewById(R.id.explore_event_button);

        eventAdapter = new EventAdapter(this, eventDataList);
        eventList.setAdapter(eventAdapter);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

//        eventDetailsInitializeActivity = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
//                        Event updatedEvent = (Event) result.getData().getSerializableExtra("UPDATED_EVENT");
//                        if(updatedEvent.getEventPromoId() != null && updatedEvent.getEventCheckInId() != null){
//                            eventDataList.add(updatedEvent);
//                            addNewEvent(updatedEvent);
//                        }
//                        eventAdapter.notifyDataSetChanged();
//                    }
//                }
//        );


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isAddButtonClicked = true;
                Event newEvent = new Event();

                String uniqueID = UUID.randomUUID().toString();
                newEvent.setEventTitle("New Event " + (eventDataList.size() +1));
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

        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    eventDataList.clear();
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
                        String eventAnnouncement = doc.getString("announcement");
                        String checkInId = doc.getString("checkInQRCodeImageUrl");
                        String promoId = doc.getString("promoQRCodeImageUrl");
                        String hostId = doc.getString("hostId");
                        HashMap<String, Long> attendeeListIdToTimes = (HashMap<String, Long>) doc.get("attendeeListIdToTimes");
                        HashMap<String, String> attendeeListIdToName = (HashMap<String, String>) doc.get("attendeeListIdToName");
                        eventDataList.add(new Event(eventTitle, eventDate, eventTime,
                                eventLocation, 0,  eventAnnouncement, checkInId, promoId, eventId,
                                hostId, attendeeListIdToTimes, attendeeListIdToName));
                        Log.d("Firestore", String.format("Event(%s %s %s %s %s %s %s %s %s) fetched", eventTitle, eventDate, eventTime, eventLocation, 0, eventAnnouncement, checkInId, promoId, eventId));
                    }
                    eventAdapter.notifyDataSetChanged();
                }
            }
        });



        eventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Event currentEvent = eventDataList.get(position);
                String userId = UserPreferences.getUserId(getApplicationContext());
                String hostId = currentEvent.getHostId();
//                if (userId.equals(hostId)) {

                    new DeleteEventFragment(currentEvent).show(getSupportFragmentManager(), "Delete Event");
//                } else {
//
//                    Toast.makeText(MainActivity.this, "You are not the host of this event.", Toast.LENGTH_SHORT).show();
//                }
                return true;
            }
        });

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
                startActivity(new Intent(MainActivity.this, AttendeeBrowseEvents.class));
            }
        });

        // User homepage attendee notifications button
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AttendeeNotifications.class));
            }
        });

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
            for (int i = 0; i < eventDataList.size(); i++) {
                if (eventDataList.get(i).getEventId().equals(updatedEvent.getEventId())) {
                    eventDataList.set(i, updatedEvent);
                    break;
                }
            }
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
        data.put("announcement", event.getAnnouncement());
//        data.put("QRCodeImage", event.getQRCodeImage());
//        data.put("PromoQRCodeImage", event.getPromoQRCodeImage());
//        data.put("eventCheckInId", event.getEventCheckInId());
//        data.put("eventPromoId", event.getEventPromoId());
        data.put("eventId", event.getEventId());
        data.put("checkInQRCodeImageUrl", event.getEventCheckInId());
        data.put("promoQRCodeImageUrl", event.getEventPromoId());
        data.put("hostId", event.getHostId());
        data.put("attendeeListIdToTimes", event.getAttendeeListIdToCheckInTimes());
        data.put("attendeeListIdToName", event.getAttendeeListIdToName());
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
     * This deletes an event from the firebase.
     * @param event This is the event to delete.
     */
    public void deleteEvent(Event event){

        eventDataList.remove(event);
        eventAdapter.notifyDataSetChanged();
        eventsRef
                .document(event.getEventId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error deleting document", e);
                    }
                });

        if (event.getEventCheckInId() != null && event.getEventPromoId() != null){
            String qrCodeFilePath = "qrcodes/" + event.getEventId() + "_" + "checkInQRCodeImageUrl" + ".jpg";
            String promoQrCodeFilePath = "qrcodes/" + event.getEventId() + "_" + "promoQRCodeImageUrl" + ".jpg";
            deleteQRCodesFromStorage(qrCodeFilePath);
            deleteQRCodesFromStorage(promoQrCodeFilePath);
        }

    }


    private void deleteQRCodesFromStorage(String filePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference().child(filePath);

        fileRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("Storage", "File successfully deleted!");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w("Storage", "Error deleting file", e);
            }
        });
    }

}
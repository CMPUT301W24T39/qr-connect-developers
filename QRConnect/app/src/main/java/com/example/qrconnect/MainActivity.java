package com.example.qrconnect;
import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.AggregateQuery;
import com.google.firebase.firestore.AggregateQuerySnapshot;
import com.google.firebase.firestore.AggregateSource;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;
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
 */

/**
 * The MainActivity class maintains the functions of the main activity.
 * It extends AppCompatActivity.
 */
public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addButton;
    private ImageButton profileButton;
    static ArrayList<Event> eventDataList = new ArrayList<Event>();
    ListView eventList;
    static boolean isAddButtonClicked = false;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    public static int numAddButtonClicked;

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

        EventAdapter eventAdapter = new EventAdapter(this, eventDataList);
        eventList.setAdapter(eventAdapter);

        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, QRCodeGeneratesPage.class);
                startActivity(intent);

                isAddButtonClicked = true;
                numAddButtonClicked += 1;
                Event newEvent = new Event();

                eventDataList.add(newEvent);
                eventAdapter.notifyDataSetChanged();

                String uniqueID = UUID.randomUUID().toString();
                newEvent.setEventTitle("New Event " + numAddButtonClicked);
                newEvent.setEventId(uniqueID);
                addNewEvent(newEvent);


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
                        eventDataList.add(new Event(eventTitle, null,null, null, null, null, null, null, null, null, eventId));
                        Log.d("Firestore", String.format("Event(%s %s %s %s %s %s %s %s %s %s %s) fetched", eventTitle, null,null, null, null, null, null, null, null, null, eventId));
                    }
                    eventAdapter.notifyDataSetChanged();
                }
            }
        });

        eventsRef.get().addOnCompleteListener(task -> {
            int count = 0;
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    count++;
                }
                Log.d(TAG, "Document count: " + count);

            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
            numAddButtonClicked = count;

        });


        eventList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deleteEvent(eventDataList.get(position));
                eventDataList.remove(eventDataList.get(position));

                eventAdapter.notifyDataSetChanged();
                return true;
            }
        });

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, UserProfilePage.class));
            }
        });

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Event currentEvent = eventAdapter.getItem(position);
                    Intent showIntent = new Intent(MainActivity.this, EventDetailsActivity.class);
                    showIntent.putExtra("EVENT_ID", currentEvent.getEventId());
                    startActivity(showIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ImageButton cameraButton = findViewById(R.id.qr_code_scanner_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, QRCodeCheckInActivity.class));
            }
        });
    }

    /**
     * This adds a new event to firebase.
     * @param event This is a event that is added to firebase.
     */
    private void addNewEvent(Event event) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("title", event.getEventTitle());
        data.put("date", event.getDate());
        data.put("time", event.getTime());
        data.put("location", event.getLocation());
        data.put("capacity", event.getCapacity());
        data.put("announcement", event.getAnnouncement());
        data.put("QRCodeImage", event.getQRCodeImage());
        data.put("PromoQRCodeImage", event.getPromoQRCodeImage());
        data.put("eventCheckInId", event.getEventCheckInId());
        data.put("eventPromoId", event.getEventPromoId());
        data.put("eventId", event.getEventId());
        data.put("checkInQRCodeImageUrl", null);
        data.put("promoQRCodeImageUrl", null);

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
    private void deleteEvent(Event event){
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

    }

}
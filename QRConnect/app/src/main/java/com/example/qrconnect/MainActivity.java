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
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addButton;

    static ArrayList<Event> eventDataList = new ArrayList<Event>();

    ListView eventList;
    static boolean isAddButtonClicked = false;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private static int numAddButtonClicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        eventList = findViewById(R.id.event_list_list);
        addButton = findViewById(R.id.button_add_event);

        EventAdapter adapter = new EventAdapter(this, eventDataList);
        eventList.setAdapter(adapter);

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
                System.out.println(numAddButtonClicked);
                newEvent.setEventTitle("A New Event " + numAddButtonClicked);
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
                        String eventTitle = doc.getId();
                        eventDataList.add(new Event(eventTitle, null,null, null, null, null, null, null, null, null));
                        Log.d("Firestore", String.format("Event(%s %s %s %s %s %s %s %s %s %s) fetched", eventTitle, null,null, null, null, null, null, null, null, null));
                    }
                    adapter.notifyDataSetChanged();
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




    }

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

        eventsRef
                .document(event.getEventTitle())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "DocumentSnapshot successfully written!");
                    }
                });
    }






}
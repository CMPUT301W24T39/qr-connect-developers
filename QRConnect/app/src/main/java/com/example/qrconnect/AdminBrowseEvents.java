package com.example.qrconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.UUID;

/**
 * The AdminBrowseEvents class manages the admin browse events page.
 * It extends AppCompatActivity.
 */
public class AdminBrowseEvents extends AppCompatActivity {
    private ListView adminEventsList;
    private ArrayList<Event> adminEventDataList;
    private AdminEventAdapter adminEventAdapter;
    private FirebaseFirestore db;
    private CollectionReference eventsRef;
    private SearchView searchView;
    private ArrayList<Event> filteredEvents;

    /**
     * Called when the activity is first created. Responsible for initializing the admin browse events page.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_events);

        // Back button to the admin menu
        ImageButton backButton = findViewById(R.id.admin_browse_events_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Notification database initialization with Firebase
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        adminEventsList = findViewById(R.id.admin_browse_events_list);
        adminEventDataList = new ArrayList<>();
        filteredEvents = new ArrayList<>();
        adminEventAdapter = new AdminEventAdapter(this, filteredEvents);
        adminEventsList.setAdapter(adminEventAdapter);

        getEvents();

        // Set up search functionality
        // Referenced https://reintech.io/blog/adding-search-functionality-android-app-searchview
        searchView = findViewById(R.id.admin_events_search_view);
        setupSearchView();

        // View event details when clicking on an event in the list
        adminEventsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Event currentEvent = adminEventAdapter.getItem(position);
                    Log.d("Admin Browse Events", "Given event: " + adminEventDataList);
                    Log.d("Admin Browse Events", "Given event list: " + currentEvent);
                    Intent showIntent = new Intent(AdminBrowseEvents.this, AdminEventDetails.class);
                    showIntent.putExtra("EVENT", currentEvent);
                    startActivity(showIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Get events from Firestore Database and update the events ListView
     */
    private void getEvents() {
        eventsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshots, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firestore", error.toString());
                    return;
                }
                if (querySnapshots != null) {
                    adminEventDataList.clear();
                    filteredEvents.clear();
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
                        String eventAnnouncement = doc.getString("announcement");
                        String checkInId = doc.getString("checkInQRCodeImageUrl");
                        String promoId = doc.getString("promoQRCodeImageUrl");
                        String hostId = doc.getString("hostId");
                        HashMap<String, Long> attendeeListIdToTimes = (HashMap<String, Long>) doc.get("attendeeListIdToTimes");
                        HashMap<String, String> attendeeListIdToName = (HashMap<String, String>) doc.get("attendeeListIdToName");
                        HashMap<String, String> signupUserIdToName = (HashMap<String, String>) doc.get("signupUserIdToName");
                        adminEventDataList.add(new Event(eventTitle, eventDate,eventTime,
                                eventLocation, 0,  eventAnnouncement, checkInId, promoId, eventId,
                                hostId, attendeeListIdToTimes, attendeeListIdToName, signupUserIdToName));
                        filteredEvents.add(new Event(eventTitle, eventDate,eventTime,
                                eventLocation, 0,  eventAnnouncement, checkInId, promoId, eventId,
                                hostId, attendeeListIdToTimes, attendeeListIdToName, signupUserIdToName));
                        Log.d("Firestore", String.format("Event(%s %s %s %s %s %s %s %s %s) fetched", eventTitle, eventDate,eventTime, eventLocation, 0, eventAnnouncement, checkInId, promoId, eventId));
                    }
                    adminEventAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Sets up the search view query (the text that is entered to filter the event search)
     */
    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    /**
     * Applies the filter from the text search to the event list
     * @param query the searched text to filter the events
     */
    private void filter(String query) {
        filteredEvents.clear();
        if (query.isEmpty()) {
            filteredEvents.addAll(adminEventDataList);
        } else {
            query = query.toLowerCase();
            for (Event event : adminEventDataList) {
                if (event.getEventTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredEvents.add(event);
                }
            }
        }
        adminEventAdapter.notifyDataSetChanged();
    }
}
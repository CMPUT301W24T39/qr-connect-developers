package com.example.qrconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The AttendeeBrowseEvents class manages the user browse events by posters page.
 * It extends AppCompatActivity.
 */
public class AttendeeBrowseEvents extends AppCompatActivity {

    private ListView listView;
    private ArrayList<Event> eventsList;
    ImageButton backButton;
    private FirebaseFirestore db;
    private SinglePosterAdapter adapter;
    private CollectionReference eventsRef;
    private SearchView searchView;
    private ArrayList<Event> filteredEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_browse_posters);

        // Initialize FirebaseFirestore
        db = FirebaseFirestore.getInstance();
        eventsRef = db.collection("events");

        listView = findViewById(R.id.eventPosterListView);
        eventsList = new ArrayList<>();
        filteredEvents = new ArrayList<>();
        adapter = new SinglePosterAdapter(this, filteredEvents);
        listView.setAdapter(adapter);

        getEvents();

        // Set up search functionality
        // Referenced https://reintech.io/blog/adding-search-functionality-android-app-searchview
        searchView = findViewById(R.id.posters_search_view);
        setupSearchView();

        // set back button listener
        backButton = findViewById(R.id.user_posters_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked event
                Event clickedEvent = filteredEvents.get(position);
                // Get the event ID of the clicked event
                String eventId = clickedEvent.getEventId();

                // Start the SignupDetailsActivity and pass the clicked event
                Intent intent = new Intent(AttendeeBrowseEvents.this, SignupDetailsActivity.class);
                intent.putExtra("eventId", eventId);
                startActivity(intent);
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
                    eventsList.clear();
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
                        String posterUrl = doc.getString("posterURL");
                        HashMap<String, Long> attendeeListIdToTimes = (HashMap<String, Long>) doc.get("attendeeListIdToTimes");
                        HashMap<String, String> attendeeListIdToName = (HashMap<String, String>) doc.get("attendeeListIdToName");
                        HashMap<String, String> attendeeListIdToLocation = (HashMap<String, String>) doc.get("attendeeListIdToLocation");
                        HashMap<String, String> signupUserIdToName = (HashMap<String, String>) doc.get("signupUserIdToName");
                        Event event = new Event(eventTitle, eventDate,eventTime,
                                eventLocation, 0,  eventAnnouncement, checkInId, promoId, eventId,
                                hostId, attendeeListIdToTimes, attendeeListIdToName, attendeeListIdToLocation, signupUserIdToName);
                        event.setEventPosterUrl(posterUrl);
                        eventsList.add(event);
                        filteredEvents.add(event);
                        Log.d("Firestore", String.format("Event(%s %s %s %s %s %s %s %s %s) fetched", eventTitle, eventDate,eventTime, eventLocation, 0, eventAnnouncement, checkInId, promoId, eventId));
                    }
                    adapter.notifyDataSetChanged();
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
            filteredEvents.addAll(eventsList);
        } else {
            query = query.toLowerCase();
            for (Event event : eventsList) {
                if (event.getEventTitle().toLowerCase().contains(query.toLowerCase())) {
                    filteredEvents.add(event);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
package com.example.qrconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.datatransport.backend.cct.BuildConfig;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.Map;

/**
 * The MapLocations class manages the check in location map page.
 * It extends AppCompatActivity.
 */
public class MapLocations extends AppCompatActivity {

    /**
     * Called when the activity is first created. Responsible for initializing the check in location map page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_locations);

        // Configuration.getInstance().setUserAgentValue(BuildConfig.APPLICATION_ID);

        MapView map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(13);
        mapController.setCenter(new GeoPoint(53.52203014477642, -113.52509220601709 ));
        mapController.animateTo(new GeoPoint(53.52203014477642, -113.52509220601709 ));

        Event currentEvent = (Event) getIntent().getSerializableExtra("EVENT");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eventRef = db.collection("events").document(currentEvent.getEventId());


        eventRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Assuming 'YourObject' is a class that represents the structure of your Firestore document.
                    Map<String, String> userLocations = (Map<String, String>) documentSnapshot.get("attendeeListIdToLocation");

                    if (userLocations!= null) {
                        for (Map.Entry<String, String> entry : userLocations.entrySet()) {

                            String coordinate = entry.getValue();
                            // Use userId and value as needed
                            if (!coordinate.equals("")){
                                String[] parts = coordinate.split(" ");

                                GeoPoint markerLocation = new GeoPoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]));

                                Marker startMarker = new Marker(map);

                                startMarker.setPosition(markerLocation);
                                startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
                                map.getOverlays().add(startMarker);
                            }

                        }
                    }
                    // Now 'object' contains your document data. You can use it as needed.
                } else {
                    Log.d("Firestore", "No such document");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Firestore", "Error getting documents: ", e);
            }
        });


        mapController.setCenter(new GeoPoint(48.8583, 2.2944));

        ImageButton backButton = findViewById(R.id.map_locations_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }
}
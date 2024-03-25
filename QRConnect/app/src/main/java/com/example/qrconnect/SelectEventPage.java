package com.example.qrconnect;

import static com.example.qrconnect.MainActivity.eventDataList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * The SelectEventPage class maintains the functions of SelectEventPage activity.
 * It extends AppCompatActivity.
 */
public class SelectEventPage extends AppCompatActivity {


    AutoCompleteTextView autoCompleteTextView;

    private ImageButton backButton2;
    Event currentEvent;
    private FirebaseFirestore db;

    /**
     * This class defines the functions of SelectEventPage activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_existing_event_qr_code);
        backButton2 = findViewById(R.id.arrow_back_2);

        autoCompleteTextView = findViewById(R.id.auto_complete_textview);
        EventAdapter adapter = new EventAdapter(this, eventDataList);
        db = FirebaseFirestore.getInstance();
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event selectedEvent = (Event) parent.getItemAtPosition(position);
                currentEvent = (Event) getIntent().getSerializableExtra("EVENT");

                String fieldNameCheckInQRCode = "checkInQRCodeImageUrl";
                String fieldNamePromoteQRCode = "promoQRCodeImageUrl";

                currentEvent.setEventCheckInId(selectedEvent.getEventCheckInId());
                currentEvent.setEventPromoId(selectedEvent.getEventPromoId());

                updateEvent(currentEvent, fieldNameCheckInQRCode, selectedEvent.getEventCheckInId());
                updateEvent(currentEvent, fieldNamePromoteQRCode, selectedEvent.getEventPromoId());

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference pathReference = storageRef.child("qrcodes/" + selectedEvent.getEventId() + "_" + "checkInQRCodeImageUrl" + ".jpg");
                pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("imageUrl", uri.toString());
                    setResult(RESULT_OK, resultIntent);
                    returnUpdatedEvent(currentEvent);
                }).addOnFailureListener(exception -> {

                });

            }
        });

        backButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnUpdatedEvent(currentEvent);
            }
        });
    }

    private void returnUpdatedEvent(Event updatedEvent) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("UPDATED_EVENT", updatedEvent);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
    private void updateEvent(Event event, String fieldName, String Id) {

        db.collection("events")
                .document(event.getEventId())
                .update(fieldName, Id);
    }


}

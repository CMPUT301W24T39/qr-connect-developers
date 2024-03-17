package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The AttendeeBrowseEvents class manages the user browse events page.
 * It extends AppCompatActivity.
 */
public class AttendeeBrowseEvents extends AppCompatActivity {

    /**
     * Called when the activity is first created. Responsible for initializing the browse events page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_browse_events);

        ImageButton backButton = findViewById(R.id.attendee_browse_events_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
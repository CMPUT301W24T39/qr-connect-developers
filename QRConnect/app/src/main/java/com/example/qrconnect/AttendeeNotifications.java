package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The AttendeeNotifications class manages the attendee notifications page.
 * It extends AppCompatActivity.
 */
public class AttendeeNotifications extends AppCompatActivity {

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

        ImageButton backButton = findViewById(R.id.attendee_notifications_page_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
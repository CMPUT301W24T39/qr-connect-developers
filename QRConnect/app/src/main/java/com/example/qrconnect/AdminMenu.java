package com.example.qrconnect;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The AdminMenu class manages the admin menu page.
 * It extends AppCompatActivity.
 */
public class AdminMenu extends AppCompatActivity {

    private ImageButton browseProfilesButton;
    private ImageButton browseImagesButton;
    private ImageButton browseEventsButton;

    /**
     * Called when the activity is first created. Responsible for initializing the view as admin menu page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_menu);

        ImageButton backButton = findViewById(R.id.admin_menu_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        browseProfilesButton = findViewById(R.id.admin_browse_profiles_button);
        browseImagesButton = findViewById(R.id.admin_browse_images_button);
        browseEventsButton = findViewById(R.id.admin_browse_events_button);

        // Admin menu browse profiles button
        browseProfilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenu.this, AdminBrowseProfiles.class));
            }
        });

        // Admin menu browse images button
        browseImagesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenu.this, AdminBrowseImages.class));
            }
        });

        // Admin menu browse events button
        browseEventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminMenu.this, AdminBrowseEvents.class));
            }
        });
    }
}
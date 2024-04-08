package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The AdminBrowseImages class manages the admin browse images page.
 * It extends AppCompatActivity.
 */
public class AdminBrowseImages extends AppCompatActivity {

    /**
     * Called when the activity is first created. Responsible for initializing the admin browse images page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_images);

        ImageButton backButton = findViewById(R.id.admin_browse_images_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
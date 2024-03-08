package com.example.qrconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * The SplashActivity class represents the initial screen displayed when the application is launched.
 * It serves as a splash screen that appears for a specific duration before transitioning to the
 * UserStartScreen activity.
 * It extends AppCompatActivity.
 */
public class SplashActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created. Responsible for initializing the splash screen.
     * @param savedInstanceState If the activity is being re-initialized after
     *      previously being shut down then this Bundle contains the data it most
     *      recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        // Post a delayed action to navigate to the UserStartScreen activity after a specified duration (3 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an intent to transition to the UserStartScreen activity
                Intent intent = new Intent(SplashActivity.this, UserStartScreen.class);
                // Start the UserStartScreen activity
                startActivity(intent);
                // Finish the current SplashActivity to prevent going back to it using the back button
                finish();
            }
        }, 3000); // 3000 milliseconds (3 seconds) delay
    }
}
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

        // Post a delayed action to navigate to the appropriate activity after a specified duration (3 seconds)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Check if the user is a returning user or a new user
                boolean isReturningUser = checkIfReturningUser(); // Implement this method

                // Create an intent based on whether the user is returning or new
                Intent intent;
                if (isReturningUser) {
                    intent = new Intent(SplashActivity.this, ReturnUserStartScreen.class);
                } else {
                    intent = new Intent(SplashActivity.this, UserStartScreen.class);
                }

                // Start the corresponding activity
                startActivity(intent);

                // Finish the current SplashActivity to prevent going back to it using the back button
                finish();
            }
        }, 3000); // 3000 milliseconds (3 seconds) delay
    }

    // Placeholder method, you need to implement the logic to determine if the user is a returning user
    private boolean checkIfReturningUser() {
        // Check if there is a stored user ID locally
        String storedUserId = UserPreferences.getUserId(getApplicationContext());

        // If a user ID exists, consider the user as returning; otherwise, they are a new user
        return storedUserId != null && !storedUserId.isEmpty();
    }
}

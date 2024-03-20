package com.example.qrconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * The UserStartScreen class represents the screen that is displayed when a new user launches the application.
 * It provides an option for the user to continue, leading to the main functionality of the application in MainActivity.
 * It extends AppCompatActivity.
 */
public class UserStartScreen extends AppCompatActivity {

    // initialize button that allows the user to proceed to the main functionality of the application.
    Button continue_button;
    Button view_as_admin_button;

    /**
     * Called when the activity is first created. Responsible for initializing the user start screen.
     * @param savedInstanceState A Bundle containing the activity's previously frozen state, if there was one.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_start_screen);
        // Obtain user ID using UserPreferences
        String obtainedUserId = UserPreferences.getUserId(getApplicationContext());

        // Initialize the continue button and set a click listener
        continue_button = findViewById(R.id.newuser_continue_button);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserPreferences.saveUserId(getApplicationContext(), obtainedUserId);
                // Start the MainActivity when the continue button is clicked
                startActivity(new Intent(UserStartScreen.this, MainActivity.class));
            }
        });

        // Initialize the view as admin button and set a click listener
        view_as_admin_button = findViewById(R.id.user_view_as_admin_button);
        view_as_admin_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserStartScreen.this, AdminQRScan.class));
            }
        });

    }
}
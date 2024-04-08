package com.example.qrconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The UserStartScreen class represents the screen that is displayed when a new user launches the application.
 * It provides an option for the user to continue, leading to the main functionality of the application in MainActivity, or
 * go to the admin authentication page.
 */
public class UserStartScreen extends AppCompatActivity {

    // initialize button that allows the user to proceed to the main functionality of the application.
    Button continue_button;
    Button view_as_admin_button;
    EditText firstNameEditText, lastNameEditText;

    /**
     * Called when the activity is first created. Responsible for initializing the user start screen.
     *
     * @param savedInstanceState A Bundle containing the activity's previously frozen state, if there was one.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_start_screen);

        // set UI buttons
        firstNameEditText = findViewById(R.id.first_name_editText);
        lastNameEditText = findViewById(R.id.last_name_editText);
        // Initialize the continue button and set a click listener
        continue_button = findViewById(R.id.newuser_continue_button);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Disable the continue button to prevent multiple clicks
                continue_button.setEnabled(false);

                // Generate random user ID
                String generatedUserId = generateRandomUserId();
                // save id in userPreferences
                UserPreferences.saveUserId(getApplicationContext(), generatedUserId);

                // Retrieve first and last names
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();
                // create new user object
                UserProfile user = new UserProfile(generatedUserId, firstName, lastName);

                // save user info to Firestore
                storeUserInFirestore(user);
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
    /**
     * Generates a random user ID.
     *
     * @return A randomly generated user ID.
     */
    private String generateRandomUserId() {
        return UUID.randomUUID().toString();
    }
    /**
     * Stores user information in Firestore.
     *
     * @param user    The user profile.
     */
    private void storeUserInFirestore(UserProfile user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String userId = user.getUserID();
        // Create a new user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("firstName", user.getFirstName());
        userData.put("lastName", user.getLastName());
        userData.put("pronouns", user.getPronouns());
        userData.put("email", user.getEmail());
        userData.put("phone", user.getPhone());
        userData.put("isLocationTrackingOn", user.getLocationTracking());
        userData.put("isProfilePictureUploaded", user.getProfilePictureUploaded());

        // Add a new document with a generated ID to the "users" collection
        db.collection("users")
                .document(userId)
                .set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Document successfully written
                        Toast.makeText(UserStartScreen.this, "User information saved successfully", Toast.LENGTH_SHORT).show();
                        // Start the MainActivity
                        startActivity(new Intent(UserStartScreen.this, MainActivity.class));
                        // Close the current activity
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle errors
                        Toast.makeText(UserStartScreen.this, "Error saving user information", Toast.LENGTH_SHORT).show();
                        // Re-enable the continue button
                        continue_button.setEnabled(true);
                    }
                });
    }

}


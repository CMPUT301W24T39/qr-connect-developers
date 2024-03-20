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
 * It provides an option for the user to continue, leading to the main functionality of the application in MainActivity.
 * It extends AppCompatActivity.
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

        // set buttons
        firstNameEditText = findViewById(R.id.first_name_editText);
        lastNameEditText = findViewById(R.id.last_name_editText);
        // Initialize the continue button and set a click listener
        continue_button = findViewById(R.id.newuser_continue_button);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Generate random user ID
                String generatedUserId = generateRandomUserId();
                // save id in userPreferences
                UserPreferences.saveUserId(getApplicationContext(), generatedUserId);
                // Retrieve first and last names
                String firstName = firstNameEditText.getText().toString().trim();
                String lastName = lastNameEditText.getText().toString().trim();

                // save user info to Firestore
                storeUserInFirestore(generatedUserId, firstName, lastName);

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

    private String generateRandomUserId() {
        // generate random uid
        return UUID.randomUUID().toString();
    }

    private void storeUserInFirestore(String userId, String firstName, String lastName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Create a new user data map
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("firstName", firstName.isEmpty() ? null : firstName); // Store null if first name is empty
        userData.put("lastName", lastName.isEmpty() ? null : lastName); // Store null if last name is empty

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
                        finish(); // Close the current activity
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        // Handle errors
                        Toast.makeText(UserStartScreen.this, "Error saving user information", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}


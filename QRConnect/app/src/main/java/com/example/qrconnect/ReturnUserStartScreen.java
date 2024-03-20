package com.example.qrconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
/**
 * The ReturnUserStartScreen class represents the screen that is displayed when a returning user launches the application.
 * It retrieves the user's information from Firestore based on the user ID and displays a welcome message.
 */
public class ReturnUserStartScreen extends AppCompatActivity {
    // initialize button that allows the user to proceed to the main functionality of the application.
    Button continue_button;
    // Textview for welcome message
    TextView welcomeBackTextView;

    /**
     * Called when the activity is first created. Responsible for initializing the returning user start screen.
     *
     * @param savedInstanceState A Bundle containing the activity's previously frozen state, if there was one.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.returning_user_startscreen);

        // Initialize the continue button and set a click listener
        continue_button = findViewById(R.id.returnUser_continue_button);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MainActivity when the continue button is clicked
                startActivity(new Intent(ReturnUserStartScreen.this, MainActivity.class));
            }
        });

        // connect view for displaying welcome message
        String userId = UserPreferences.getUserId(getApplicationContext());
        welcomeBackTextView = findViewById(R.id.welcome_back_text);
        getUserInfoFromFirestore(userId);
    }

    /**
     * Retrieves the user's information from Firestore based on the provided user ID.
     *
     * @param userId The user ID used to retrieve the user's information from Firestore.
     */
    private void getUserInfoFromFirestore(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Get document reference for the user
        DocumentReference userRef = db.collection("users").document(userId);

        // Retrieve user's information from Firestore
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    // Document exists, retrieve first and last names
                    String firstName = documentSnapshot.getString("firstName");
                    String lastName = documentSnapshot.getString("lastName");

                    // Update welcome back text with the user's name
                    String welcomeMessage = "Welcome back, " + firstName + " " + lastName;
                    welcomeBackTextView.setText(welcomeMessage);
                } else {
                    // Document does not exist, log the event
                    Log.d("Firestore", "User document does not exist for the specified user ID");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Failed to retrieve user's information. Display error message
                Toast.makeText(ReturnUserStartScreen.this, "Error retrieving  user information", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.qrconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The SplashActivity class represents the initial screen displayed when the application is launched.
 * It serves as a splash screen that appears for a specific duration before transitioning to the
 * UserStartScreen activity.
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
                String storedUserId = UserPreferences.getUserId(getApplicationContext());
                boolean isReturningUser = checkIfReturningUser(storedUserId);

                // Create an intent based on whether the user is returning or new
                final Intent[] intent = new Intent[1];
                if (isReturningUser) {
                    // checks if locally stored user ID belongs to an existing user on firebase
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("users")
                        .document(storedUserId).get().addOnSuccessListener(
                            new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    if (documentSnapshot.exists()) {
                                        // goes to ReturnUserStartScreen if user ID matches
                                        intent[0] = new Intent(SplashActivity.this, ReturnUserStartScreen.class);
                                    } else {
                                        // clears user ID if it does not exist in Firebase
                                        UserPreferences.clearUserId(getApplicationContext());
                                        intent[0] = new Intent(SplashActivity.this, UserStartScreen.class);
                                    }
                                    // Start the corresponding activity
                                    startActivity(intent[0]);
                                    // Finish the current SplashActivity to prevent going back to it using the back button
                                    finish();
                                }
                            }
                        ).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firestore", "Error fetching data");
                            }
                        });
                } else {
                    // goes to UserStartScreen if no user ID is found on device
                    intent[0] = new Intent(SplashActivity.this, UserStartScreen.class);
                    // Start the corresponding activity
                    startActivity(intent[0]);
                    // Finish the current SplashActivity to prevent going back to it using the back button
                    finish();
                }
            }
        }, 3000); // 3000 milliseconds (3 seconds) delay
    }

    /**
     * Checks if current user is already registered in the app
     * @return true if user is already registered; false otherwise
     */
    private boolean checkIfReturningUser(String storedUserId) {
            return (storedUserId != null && !storedUserId.isEmpty());
    }
}

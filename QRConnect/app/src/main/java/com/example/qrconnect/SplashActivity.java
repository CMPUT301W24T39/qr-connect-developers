package com.example.qrconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * The SplashActivity class represents the initial screen displayed when the application is launched.
 * It serves as a splash screen that appears for a specific duration before transitioning to the
 * UserStartScreen activity.
 * It extends AppCompatActivity.
 */
public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

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
                checkSignIn();
            }
        }, 3000); // 3000 milliseconds (3 seconds) delay
    }
    private void checkSignIn() {
        // Source: https://firebase.google.com/docs/auth/android/anonymous-auth#java

        FirebaseAuth.getInstance().signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInAnonymously:success");
                            // User signed in anonymously
                            checkUserType();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SplashActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void checkUserType() {
        // Check if the user is already signed in anonymously
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null && currentUser.isAnonymous()) {
            // Returning user (already signed in anonymously)
            startActivity(new Intent(SplashActivity.this, ReturnUserStartScreen.class));
        } else {
            // New user not signed in anonymously
            startActivity(new Intent(SplashActivity.this, UserStartScreen.class));
        }

        finish();
    }


}
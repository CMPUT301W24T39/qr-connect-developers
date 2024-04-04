package com.example.qrconnect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * The AdminProfileDetails class manages the admin profile details page so the admin has the option to delete the profile.
 * It extends AppCompatActivity.
 */
public class AdminProfileDetails extends AppCompatActivity implements AdminDeleteProfileFragment.AdminDeleteProfileDialogListener {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private DocumentReference userRef;

    /**
     * Initializes the activity, sets the content view, and begins the process of loading profile details.
     * It retrieves the user ID passed from the previous activity and uses it to load the corresponding
     * profile details from Firestore and the profile picture from Firebase Storage.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState.
     *                           Otherwise, it is null.
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_profiles_profile_details);

        // Get items from previous activity
        String userId = getIntent().getStringExtra("PROFILE");

        // Get event details for the event that was clicked on
        loadProfileDetails(userId);

        // Back button to the admin event browse page
        ImageButton backButton = findViewById(R.id.admin_profile_details_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Delete button
        Button deleteButton = findViewById(R.id.admin_delete_profile_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager() != null) {
                    new AdminDeleteProfileFragment(userId, AdminProfileDetails.this).show(getSupportFragmentManager(), "Delete Profile");
                }
            }
        });
    }

    /**
     * Fetches and displays the details of a profile from Firestore.
     * It also retrieves the profile picture image from Firebase Storage and displays it using Glide.
     *
     * @param userID The unique identifier of the user whose details are to be loaded and displayed.
     */
    private void loadProfileDetails(String userID) {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");
        userRef = usersRef.document(userID);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Set text fields with data from Firestore
                ImageView profilePicture = findViewById(R.id.admin_profile_picture);
                TextView firstName = findViewById(R.id.admin_profile_first_name);
                TextView lastName = findViewById(R.id.admin_profile_last_name);
                TextView pronouns = findViewById(R.id.admin_profile_pronouns);
                TextView email = findViewById(R.id.admin_profile_email);
                TextView phone = findViewById(R.id.admin_profile_phone);
                TextView location = findViewById(R.id.admin_profile_location);

                firstName.setText(documentSnapshot.getString("firstName"));
                lastName.setText(documentSnapshot.getString("lastName"));

                /*pronouns.setText(documentSnapshot.getString("pronouns"));
                email.setText(documentSnapshot.getString("email"));
                phone.setText(documentSnapshot.getString("phone"));
                boolean tracking = documentSnapshot.getBoolean("isLocationTrackingOn");
                if (tracking == false) {
                    location.setText("Disabled");
                if (tracking == true) {
                    location.setText("Enabled");*/

                // TODO: Implement the rest of the profile information.

            } else {
                Log.d("ProfileDetails", "Document does not exist.");
            }
        }).addOnFailureListener(e -> {
            Log.d("ProfileDetails", "Error fetching document: ", e);
        });
    }

    /**
     * This deletes an event from the firebase.
     *
     * @param userId the user id of the profile that is being deleted.
     */
    public void deleteProfile(String userId) {

        usersRef
                .document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Firestore", "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("Firestore", "Error deleting document", e);
                    }
                });
        finish();
    }
}
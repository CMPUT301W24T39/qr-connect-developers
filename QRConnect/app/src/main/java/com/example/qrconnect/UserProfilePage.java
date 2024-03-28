package com.example.qrconnect;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

/**
 * The UserProfilePage class is for the user profile page.
 * Contains all of the information of a user such as name, pronouns, contact information (email and phone), and their profile picture.
 * It extends AppCompatActivity.
 */
public class UserProfilePage extends AppCompatActivity {
    private String USER_ID;
    private static String COLLECTION_PATH = "users";
    private static String STORAGE_PATH = "profile_pictures/";
    private ImageView profilePicture;
    private Button addPhotoButton;
    private Button removePhotoButton;
    private Button saveButton;
    private Switch locationSwitch;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText pronounsEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;

    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // Hardcoded to single user for now.
    // TODO: allow multiple users.
    private User user = new User(USER_ID, "", "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);
        USER_ID = UserPreferences.getUserId(getApplicationContext());

        initializeFirebase();
        findViews();
        getUserData();

        handleAddPhotoButton();
        handleRemovePhotoButton();
        handleSaveButton();
    }

    /**
     * Initializes Firebase database and storage
     */
    private void initializeFirebase() {
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(COLLECTION_PATH);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    /**
     * Initializes all views
     */
    private void findViews() {
        profilePicture = findViewById(R.id.profile_picture);

        addPhotoButton = findViewById(R.id.add_photo_button);
        removePhotoButton = findViewById(R.id.remove_photo_button);

        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameEditText = findViewById(R.id.last_name_edit);
        pronounsEditText = findViewById(R.id.pronouns_edit);
        emailEditText = findViewById(R.id.email_edit);
        phoneEditText =  findViewById(R.id.phone_edit);

        locationSwitch = findViewById(R.id.location_switch);

        saveButton = findViewById(R.id.save_button);
    }

    /**
     * Gets user data from Firebase and updates the profile page with said data.
     */
    private void getUserData() {
        usersRef.document(USER_ID).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        setUserData(documentSnapshot);
                        setEditTextData();
                        setProfilePicture();
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error fetching user data");
                // TODO: handle error
            }
        });
    }

    /**
     * This function sets the values of the user object to those on Firebase
     * @param documentSnapshot Firebase documentSnapshot
     */
    private void setUserData(DocumentSnapshot documentSnapshot) {
        user.setEmail(documentSnapshot.getString("email"));
        user.setFirstName(documentSnapshot.getString("firstName"));
        user.setLocationTracking(documentSnapshot.getBoolean("isLocationTrackingOn"));
        user.setProfilePictureUploaded(documentSnapshot.getBoolean("isProfilePictureUploaded"));
        user.setLastName(documentSnapshot.getString("lastName"));
        user.setPhone(documentSnapshot.getString("phone"));
        user.setPronouns(documentSnapshot.getString("pronouns"));
        user.setProfilePictureURL(documentSnapshot.getString("profilePictureURL"));
        Log.d("user", user.getLastName());
    }

    /**
     * This function sets the edit text data to match the values of the user object
     */
    private void setEditTextData() {
        firstNameEditText.setText(user.getFirstName());
        lastNameEditText.setText(user.getLastName());
        pronounsEditText.setText(user.getPronouns());
        emailEditText.setText(user.getEmail());
        phoneEditText.setText(user.getPhone());
        Log.d("here", "here");
        locationSwitch.setChecked(user.getLocationTracking());
    }

    /**
     * Sets profile picture if it exists, sets a generated one if it doesn't
     */
    private void setProfilePicture() {
        if (user.getProfilePictureUploaded()) {
            Glide.with(this).load(user.getProfilePictureURL()).into(profilePicture);
        } else {
            profilePicture.setImageBitmap(AvatarGenerator.generateAvatar(user));
        }
    }

    /**
     * Opens up image gallery on selection of add photo button.
     * From here, users can select a photo to be used as their profile picture
     */
    private void handleAddPhotoButton() {
        // The following function from https://developer.android.com/training/data-storage/shared/photopicker#java, Downloaded 2024-03-07
        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        saveImageToFirebase(uri);
                        profilePicture.setImageURI(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // The following function from https://developer.android.com/training/data-storage/shared/photopicker#java, Downloaded 2024-03-07
                // Launch the photo picker and let the user choose only images.
                pickMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());
            }
        });
    }

    /**
     * Saves the added profile picture to firebase
     * @param uri Uri of new profile picture
     */
    private void saveImageToFirebase(Uri uri) {
        StorageReference imageRef = storageRef.child(STORAGE_PATH + user.getUserID() + ".png");

        imageRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    user.setProfilePictureURL(url);
                    user.setProfilePictureUploaded(true);
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("profilePictureURL", url);
                    data.put("isProfilePictureUploaded", true);
                    usersRef.document("1").update(data).addOnSuccessListener(
                            new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("Firestore", "DocumentSnapshot successfully written");
                                }
                            }
                    );
                }).addOnFailureListener(exception ->
                                Log.e("Firebase", "Failed to download profile picture URL")
                        // TODO: handle error
                );
            }
        }).addOnFailureListener(exception ->
                        Log.e("Firebase", "Failed to upload profile picture to Firebase")
                //TODO: handle error
        );
    }

    /**
     * Removes profile picture and replaces it with a generated one
     */
    private void handleRemovePhotoButton() {
        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePicture.setImageBitmap(AvatarGenerator.generateAvatar(user));
                user.setProfilePictureUploaded(false);
                HashMap<String, Object> data = new HashMap<>();
                data.put("isProfilePictureUploaded", false);
                usersRef.document("1").update(data).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Firestore", "DocumentSnapshot successfully written");
                            }
                        }
                );
            }
        });
    }

    /**
     * Saves the data that the user inputted
     */
    private void handleSaveButton() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
                // update generated bitmap to match current name.
                // do not do this if user has uploaded their own profile picture
                if (!user.getProfilePictureUploaded()) {
                    profilePicture.setImageBitmap(AvatarGenerator.generateAvatar(user));
                }
            }
        });
    }

    /**
     * This function saves the inputted data to firebase and updates the user object
     */
    private void saveUserData() {
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String pronouns = pronounsEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String phone = phoneEditText.getText().toString();
        Boolean isLocationTrackingOn = locationSwitch.isChecked();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPronouns(pronouns);
        user.setEmail(email);
        user.setPhone(phone);
        user.setLocationTracking(isLocationTrackingOn);

        HashMap<String, Object> data = new HashMap<>();
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("pronouns", pronouns);
        data.put("email", email);
        data.put("phone", phone);
        data.put("isLocationTrackingOn", isLocationTrackingOn);

        // Hardcoded to single user for now.
        // TODO: allow multiple users.
        usersRef.document(USER_ID).update(data).addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Firestore", "DocumentSnapshot successfully written");
                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Firestore", "Error writing user data to Firebase");
                // TODO: handle error
            }
        });
    }
}

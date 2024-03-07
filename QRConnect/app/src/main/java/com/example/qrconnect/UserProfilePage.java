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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class UserProfilePage extends AppCompatActivity {
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);

        profilePicture = findViewById(R.id.profile_picture);
        profilePicture.setImageResource(R.drawable.profile_icon_9);

        addPhotoButton = findViewById(R.id.add_photo_button);
        removePhotoButton = findViewById(R.id.remove_photo_button);

        saveButton = findViewById(R.id.save_button);
        locationSwitch = findViewById(R.id.location_switch);

        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameEditText = findViewById(R.id.last_name_edit);
        pronounsEditText = findViewById(R.id.pronouns_edit);
        emailEditText = findViewById(R.id.email_edit);
        phoneEditText =  findViewById(R.id.phone_edit);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String pronouns = pronounsEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();

                Boolean isLocationTrackingOn = locationSwitch.isChecked();
                Log.d("switch", String.valueOf(isLocationTrackingOn));

                /*
                TODO: connect this to firebase
                 */
            }
        });

        // The following function from https://developer.android.com/training/data-storage/shared/photopicker#java, Downloaded 2024-03-07
        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        // TODO: save this image to firebase
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

        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                TODO: this function should revert profile picture to a procedurally generated image
                 */
                profilePicture.setImageResource(R.drawable.profile_icon_9);
            }
        });

    }
}


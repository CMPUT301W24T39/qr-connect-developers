package com.example.qrconnect;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
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
        profilePicture.setImageBitmap(generateAvatar());

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
                should also update generated avatar if no profile picture has been added
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
                profilePicture.setImageBitmap(generateAvatar());
            }
        });
    }

    /**
     * This function returns a deterministically generated bitmap based on a hash of the user's name
     * to be used as the user's profile picture if they do not upload one themselves
     * @return Bitmap To be used as the profile picture
     */
    private Bitmap generateAvatar() {
        // temporary. should get names from firebase
        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameEditText = findViewById(R.id.last_name_edit);
        String firstName = firstNameEditText.getText().toString();
        String lastName = lastNameEditText.getText().toString();
        String name = firstName + " " + lastName;

        try {
            // generate hash of user's name and convert to byte array
            // TODO: should be user's name + user ID
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] sha = md.digest(name.getBytes());

            // convert bytes to string of their binary representation
            // pad with zeros to keep it 16x16. this pads on the wrong side but it doesn't make a difference
            String binaryString = "";
            for (byte b : sha) {
                String s = Integer.toBinaryString(b & 0xFF);
                while (s.length() < 8) {
                    s += "0";
                }
                binaryString += s;
            }

            // create bitmap and then fill its pixels in with the values of the binary string
            Bitmap bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.RGB_565);
            for (int i=0; i<16; i++) {
                for (int j=0; j<16; j++) {
                    int pixel;
                    if (binaryString.charAt(16*i + j) == '1') {
                        pixel = 0xFFFFFF;
                    } else {
                        pixel = 0x000000;
                    }
                    // this sets each 16x16 grid in the 256x256 bitmap to the given pixel
                    // same as scaling the image up, but without a loss in quality
                    for (int ii = 16*i; ii < 16*i + 16; ii++) {
                        for (int jj = 16*j; jj < 16*j + 16; jj++) {
                            bitmap.setPixel(ii,jj,pixel);
                        }
                    }
                    // alternative approach: a 16x16 grid of 16x16 bitmaps
//                    for (int ii=0; ii<16; ii++) {
//                        for (int jj=0; jj<16; jj++) {
//                            bitmap.setPixel(ii*16 + i, jj*16+j,pixel);
//                        }
//                    }
                    // just a 16x16 bitmap
                    //bitmap.setPixel(i,j,pixel);
                }
            }
            Log.d("byte array", Arrays.toString(sha));
            return bitmap;
        } catch (NoSuchAlgorithmException e){
            Log.e("SHA error", "NoSuchAlgorithmException");
        }
        return null;
    }
}


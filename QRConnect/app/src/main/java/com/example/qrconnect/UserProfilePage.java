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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
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

    private FirebaseFirestore db;
    private UserProfile user = new UserProfile("1");
    private CollectionReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        findViews();

        getUserData();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserData();
                /*
                should also update generated avatar if no profile picture has been added
                 */
            }
        });

        handleAddPhotoButton();

        removePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profilePicture.setImageBitmap(generateAvatar());
            }
        });
    }

    /**
     * Gets user data from Firebase and updates the profile page with said data.
     */
    private void getUserData() {
        usersRef.document("1").get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        setUserData(documentSnapshot);
                        setEditTextData();
                        profilePicture.setImageBitmap(generateAvatar());
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
     * This function sets the values of the user object to those on Firebase
     * @param documentSnapshot Firebase documentSnapshot
     */
    private void setUserData(DocumentSnapshot documentSnapshot) {
        user.setFirstName(documentSnapshot.getString("firstName"));
        user.setLastName(documentSnapshot.getString("lastName"));
        user.setPronouns(documentSnapshot.getString("pronouns"));
        user.setEmail(documentSnapshot.getString("email"));
        user.setPhone(documentSnapshot.getString("phone"));
        user.setLocationTracking(documentSnapshot.getBoolean("isLocationTrackingOn"));
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
        locationSwitch.setChecked(user.getLocationTracking());
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

        usersRef.document("1").set(data).addOnSuccessListener(
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
    }

    /**
     * This function returns a deterministically generated bitmap based on a hash of the user's name
     * to be used as the user's profile picture if they do not upload one themselves
     * @return Bitmap To be used as the profile picture
     */
    private Bitmap generateAvatar() {
        // This function should be moved to its own class
        String firstName = user.getFirstName();
        String lastName = user.getLastName();
        String userID = user.getUserID();

        String name = firstName + " " + lastName + userID;

        try {
            // generate hash of user's name and convert to byte array
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


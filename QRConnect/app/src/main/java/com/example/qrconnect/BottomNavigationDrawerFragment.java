package com.example.qrconnect;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * The BottomNavigationDrawerFragment class manages the drop down menu after long clicks on a QR code.
 * It extends BottomSheetDialogFragment.
 */
public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {

    public interface EventInformationProvider {
        String getCheckInId();
        void onQRCodeUploaded(String downloadUrl);
    }

    private EventInformationProvider eventInformationProvider;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EventInformationProvider) {
            eventInformationProvider = (EventInformationProvider) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement EventInformationProvider");
        }

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            uploadImageToFirebase(selectedImageUri);
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView= inflater.inflate(R.layout.bottom_nav_layout, container, false);

        NavigationView navigationView=rootView.findViewById(R.id.navigation_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int itemId = menuItem.getItemId();
                if (itemId == R.id.nav_download) {

                    return true;
                } else if (itemId == R.id.nav_upload) {
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    activityResultLauncher.launch(intent);
                    return true;
                } else if (itemId == R.id.nav_turn_right){
                    ((QRCodeGeneratesPage)getActivity()).launchSelectEventPage();
                    dismiss();
                    return true;
                }
                return false;
            }

        });


        return rootView;
    }

    private void uploadImageToFirebase(Uri imageUri) {
        String checkInId = eventInformationProvider.getCheckInId();
        // Define the path in Firebase Storage
        StorageReference fileRef = storageRef.child("qrcodes/" + checkInId);
        System.out.println("THIS IS A DEBUG MESSAGE");
        UploadTask uploadTask = fileRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
            Toast.makeText(getContext(), "Check-in QR code is successfully uploaded", Toast.LENGTH_SHORT).show();
            eventInformationProvider.onQRCodeUploaded(uri.toString());
        })).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Check-in QR code is not successfully uploaded", Toast.LENGTH_SHORT).show();
        });
    }

}
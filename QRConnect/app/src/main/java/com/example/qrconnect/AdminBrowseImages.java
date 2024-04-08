package com.example.qrconnect;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The AdminBrowseImages class manages the admin browse images page.
 * It extends AppCompatActivity.
 */
public class AdminBrowseImages extends AppCompatActivity {

    private ListView listViewImages;
    private List<ImageInfo> imageUrls;
    private ImagesAdapter adapter;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_images);

        listViewImages = findViewById(R.id.admin_list_view_images);
        imageUrls = new ArrayList<>();
        adapter = new ImagesAdapter(this, imageUrls);
        listViewImages.setAdapter(adapter);
        backButton = findViewById(R.id.admin_browse_images_back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        loadImages();

        listViewImages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new AlertDialog.Builder(AdminBrowseImages.this)
                        .setTitle("Do you want to remove this image from list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = imageUrls.get(position).getName();
                                String profileImagePath = "profile_pictures/" + name + ".png";
                                String posterImagePath = "eventposters/" + name + "_eventPoster.jpg";
                                checkAndDeleteImage(profileImagePath);
                                checkAndDeleteImage(posterImagePath);
                                deleteDataByName(name);
                                imageUrls.remove(position);
                                adapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                return true;
            }
        });
    }

    /**
     * This method load the images from realtime database to the screen
     */
    private void loadImages() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("images");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageUrls.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    String url = postSnapshot.child("url").getValue(String.class);
                    if (name != null && url != null) {
                        imageUrls.add(new ImageInfo(name, url));
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    /**
     * This method checks if the image is in the path "eventposters/" or "profile_pictures"
     * @param imagePath the path of the image
     */
    private void checkAndDeleteImage(String imagePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReference().child(imagePath);

        imageRef.delete().addOnSuccessListener(aVoid -> {
            Log.d("DeleteImage", imagePath + " has been deleted from Firebase Storage.");
        }).addOnFailureListener(exception -> {
            Log.d("DeleteImage", "Failed to delete " + imagePath + " from Firebase Storage: " + exception.getMessage());
        });
    }

    /**
     * Delete the image using its name as the reference
     * @param targetName
     */
    public void deleteDataByName(String targetName) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("images");
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean isFound = false;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    if (targetName.equals(name)) {
                        postSnapshot.getRef().removeValue()
                                .addOnSuccessListener(aVoid -> {
                                    Log.d("DeleteOperation", "Successfully deleted the record with name: " + targetName);
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("DeleteOperation", "Failed to delete the record with name: " + targetName, e);
                                });
                        isFound = true;
                        break;
                    }
                }
                if (!isFound) {
                    Log.d("DeleteOperation", "No record found with name: " + targetName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("DeleteOperation", "Database error: " + databaseError.getMessage());
            }
        });
    }

}
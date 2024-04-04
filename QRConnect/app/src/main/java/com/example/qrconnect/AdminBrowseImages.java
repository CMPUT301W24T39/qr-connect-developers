package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
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
    }

    private void loadImages() {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("eventposters");
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



}
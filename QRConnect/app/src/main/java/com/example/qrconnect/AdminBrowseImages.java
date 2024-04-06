package com.example.qrconnect;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * The AdminBrowseImages class manages the admin browse images page.
 * It extends AppCompatActivity.
 */
public class AdminBrowseImages extends AppCompatActivity {
    private ListView adminImagesList;
    private ArrayList<UserProfile> adminImageDataList;
    private AdminImageAdapter adminImageAdapter;
    private FirebaseFirestore db;
    private CollectionReference imagesRef;
    private SearchView searchView;
    private ArrayList<UserProfile> filteredImages;

    /**
     * Called when the activity is first created. Responsible for initializing the admin browse images page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_images);

        ImageButton backButton = findViewById(R.id.admin_browse_images_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();
        imagesRef = db.collection("users");

        adminImagesList = findViewById(R.id.admin_browse_images_list);
        adminImageDataList = new ArrayList<>();
        filteredImages = new ArrayList<>();
        adminImageAdapter = new AdminImageAdapter(this, filteredImages);
        adminImagesList.setAdapter(adminImageAdapter);

        getImages();

        // Set up search functionality
        // Referenced https://reintech.io/blog/adding-search-functionality-android-app-searchview
        searchView = findViewById(R.id.admin_images_search_view);
        setupSearchView();
    }

    /**
     * Get profiles from Firestore Database and update the profiles ListView
     */
    private void getImages() {
        imagesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshots) {
                adminImageDataList.clear();
                filteredImages.clear();
                for (QueryDocumentSnapshot doc: querySnapshots){
                    String firstName = doc.getString("firstName");
                    String lastName = doc.getString("lastName");
                    String userId = doc.getString("userId");
                    boolean isProfilePictureUploaded = doc.getBoolean("isProfilePictureUploaded");
                    String profilePictureURL = doc.getString("profilePictureURL");

                    UserProfile userProfile = new UserProfile(userId, firstName, lastName);
                    userProfile.setProfilePictureUploaded(isProfilePictureUploaded);
                    userProfile.setProfilePictureURL(profilePictureURL);

                    adminImageDataList.add(userProfile);
                    filteredImages.add(userProfile);
                }
                adminImageAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void filter(String query) {
        filteredImages.clear();
        if (query.isEmpty()) {
            filteredImages.addAll(adminImageDataList);
        } else {
            query = query.toLowerCase();
            for (UserProfile profile : adminImageDataList) {
                String profileName = profile.getFirstName() + " " + profile.getLastName();
                if (profileName.toLowerCase().contains(query.toLowerCase())) {
                    filteredImages.add(profile);
                }
            }
        }
        adminImageAdapter.notifyDataSetChanged();
    }
}
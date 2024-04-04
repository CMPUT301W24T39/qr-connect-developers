package com.example.qrconnect;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * The AdminBrowseProfiles class manages the admin browse profiles page.
 * It extends AppCompatActivity.
 */
public class AdminBrowseProfiles extends AppCompatActivity {
    private ListView adminProfilesList;
    private ArrayList<UserProfile> adminProfileDataList;
    private AdminProfileAdapter adminProfileAdapter;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private SearchView searchView;
    private ArrayList<UserProfile> filteredProfiles;

    /**
     * Called when the activity is first created. Responsible for initializing the admin browse profiles page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_browse_profiles);

        ImageButton backButton = findViewById(R.id.admin_browse_profiles_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Notification database initialization with Firebase
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("users");

        adminProfilesList = findViewById(R.id.admin_browse_profiles_list);
        adminProfileDataList = new ArrayList<>();
        filteredProfiles = new ArrayList<>();
        adminProfileAdapter = new AdminProfileAdapter(this, filteredProfiles);
        adminProfilesList.setAdapter(adminProfileAdapter);

        getProfiles();

        // Set up search functionality
        // Referenced https://reintech.io/blog/adding-search-functionality-android-app-searchview
        searchView = findViewById(R.id.admin_profiles_search_view);
        setupSearchView();

        // View event details when clicking on an event in the list
        adminProfilesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    UserProfile currentProfile = adminProfileAdapter.getItem(position);
                    Log.d("AdminBrowseProfiles", "Selected profile: " + currentProfile);
                    String currentProfileId = currentProfile.getUserID();
                    Intent showIntent = new Intent(AdminBrowseProfiles.this, AdminProfileDetails.class);
                    showIntent.putExtra("PROFILE", currentProfileId);
                    startActivity(showIntent);
                } catch (Exception e) {
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Get profiles from Firestore Database and update the profiles ListView
     */
    private void getProfiles() {
        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshots) {
                adminProfileDataList.clear();
                filteredProfiles.clear();
                for (QueryDocumentSnapshot doc: querySnapshots){
                    String firstName = doc.getString("firstName");
                    String lastName = doc.getString("lastName");
                    String userId = doc.getString("userId");

                    //String email = doc.getString("email");
                    //String phone = doc.getString("phone");
                    //String pronouns = doc.getString("pronouns");
                    //boolean isLocationTrackingOn = doc.getBoolean("isLocationTrackingOn");
                    //boolean isProfilePictureUploaded = doc.getBoolean("isProfilePictureUploaded");
                    //String profilePictureURL = doc.getString("profilePictureURL");

                    // TODO: Implement the rest of the profile information.

                    UserProfile userProfile = new UserProfile(userId, firstName, lastName);
                    userProfile.setFirstName(firstName);
                    userProfile.setLastName(lastName);
                    userProfile.setUserID(userId);

                    adminProfileDataList.add(userProfile);
                    filteredProfiles.add(userProfile);
                }
                adminProfileAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * Sets up the search view query (the text that is entered to filter the profile search)
     */
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

    /**
     * Applies the filter from the text search to the profile list
     * @param query the searched text to filter the profiles
     */
    private void filter(String query) {
        filteredProfiles.clear();
        if (query.isEmpty()) {
            filteredProfiles.addAll(adminProfileDataList);
        } else {
            query = query.toLowerCase();
            for (UserProfile profile : adminProfileDataList) {
                String profileName = profile.getFirstName() + " " + profile.getLastName();
                if (profileName.toLowerCase().contains(query.toLowerCase())) {
                    filteredProfiles.add(profile);
                }
            }
        }
        adminProfileAdapter.notifyDataSetChanged();
    }
}
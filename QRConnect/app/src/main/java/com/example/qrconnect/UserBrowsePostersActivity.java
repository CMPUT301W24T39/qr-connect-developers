package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class UserBrowsePostersActivity extends AppCompatActivity {
    private ListView listView;
    private List<Event> eventsList;
    ImageButton backButton = findViewById(R.id.user_posters_back_button);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_browse_posters);

        // Retrieve the list of events from the intent extra
        eventsList = (List<Event>) getIntent().getSerializableExtra("events");


        // Initialize ListView and set up the adapter
        listView = findViewById(R.id.eventPosterListView);
        EventAdapter adapter = new EventAdapter(this, (ArrayList<Event>) eventsList);
        listView.setAdapter(adapter);

        // set back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


}

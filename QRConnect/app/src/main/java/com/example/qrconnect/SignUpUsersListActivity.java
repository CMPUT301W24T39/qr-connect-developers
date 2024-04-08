package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpUsersListActivity extends AppCompatActivity {
    private ArrayList<DisplayAttendee> attendees;
    private AttendeeArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_signup_users_list);
        Event currentEvent = (Event) getIntent().getSerializableExtra("EVENT");
        attendees = generateSignUpUsers(currentEvent);

        ListView signUpUserListView = findViewById(R.id.show_signup_user_list_view);
        ImageButton backButton = findViewById(R.id.signup_list_back_nav_button);

        // Create adapter and set it to the ListView
        adapter = new AttendeeArrayAdapter(this, attendees, currentEvent.getEventId());
        signUpUserListView.setAdapter(adapter);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private ArrayList<DisplayAttendee> generateSignUpUsers(Event event) {
        ArrayList<DisplayAttendee> signUpUsers = new ArrayList<>();
        HashMap<String, String> signUpUsersIdToName = event.getSignupUserIdToName();

        for (Map.Entry<String, String> entry : signUpUsersIdToName.entrySet()) {
            String userId = entry.getKey();
            String userName = entry.getValue();
            DisplayAttendee signUpUser = new DisplayAttendee(userId, userName);
            signUpUsers.add(signUpUser);
        }

        return signUpUsers;
    }
}



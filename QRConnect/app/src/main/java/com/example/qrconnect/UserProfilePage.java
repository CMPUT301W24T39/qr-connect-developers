package com.example.qrconnect;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class UserProfilePage extends AppCompatActivity {

    private Button saveButton;
    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText pronounsEditText;
    private EditText emailEditText;
    private EditText phoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);

//        addCityEditText = findViewById(R.id.city_name_edit);
        firstNameEditText = findViewById(R.id.first_name_edit);
        lastNameEditText = findViewById(R.id.last_name_edit);
        pronounsEditText = findViewById(R.id.pronouns_edit);
        emailEditText = findViewById(R.id.email_edit);
        phoneEditText = findViewById(R.id.phone_edit);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameEditText.getText().clear();
                lastNameEditText.getText().clear();
                pronounsEditText.getText().clear();
                emailEditText.getText().clear();
                phoneEditText.getText().clear();

                final String firstName = firstNameEditText.getText().toString();
                final String lastName = lastNameEditText.getText().toString();
                final String pronouns = pronounsEditText.getText().toString();
                final String email = emailEditText.getText().toString();
                final String phone = phoneEditText.getText().toString();
            }
        });

    }
}


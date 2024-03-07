package com.example.qrconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

public class UserProfilePage extends AppCompatActivity {

    private Button saveButton;
    private TextInputEditText firstNameEditText;
    private TextInputEditText lastNameEditText;
    private TextInputEditText pronounsEditText;
    private TextInputEditText emailEditText;
    private TextInputEditText phoneEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile_page);

        saveButton = findViewById(R.id.save_button);

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

                /*
                TODO: connect this to firebase
                 */
            }
        });

    }
}


package com.example.qrconnect;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReturnUserStartScreen extends AppCompatActivity {
    Button continue_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.returning_user_startscreen);
        // Initialize the continue button and set a click listener
        continue_button = findViewById(R.id.return_user_cnntinue);
        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the MainActivity when the continue button is clicked
                startActivity(new Intent(ReturnUserStartScreen.this, MainActivity.class));
            }
        });

    }
}
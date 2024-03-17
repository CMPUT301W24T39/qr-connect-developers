package com.example.qrconnect;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PromoDetailsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String eventId = getIntent().getStringExtra("EVENT_ID");
        Log.d("Promo Details Activity", "Received event ID: " + eventId);
        setContentView(R.layout.activity_promo_detatils);
        ImageButton backButton = findViewById(R.id.promo_details_back_nav_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

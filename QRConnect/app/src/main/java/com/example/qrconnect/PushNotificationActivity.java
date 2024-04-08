package com.example.qrconnect;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class maintains the functions of pushing notifications
 */
public class PushNotificationActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.push_notification);
        textView = findViewById(R.id.push_notification_text);
        String data = getIntent().getStringExtra("data");
        textView.setText(data);
    }
}
package com.example.qrconnect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

/**
 * The ViewAsAdmin class manages the view as admin scan QR code page.
 * It extends AppCompatActivity.
 */
public class AdminQRScan extends AppCompatActivity {

    Button enter_token_button;

    /**
     * Called when the activity is first created. Responsible for initializing the view as admin scan QR code page.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_qr_scan);

        ImageButton backButton = findViewById(R.id.admin_qr_scan_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Initialize the enter token button and set a click listener
        enter_token_button = findViewById(R.id.admin_enter_token_button);
        enter_token_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminQRScan.this, AdminMenu.class));
            }
        });
    }
}

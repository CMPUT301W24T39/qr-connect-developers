package com.example.qrconnect;

import static com.example.qrconnect.MainActivity.eventDataList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class QRCodeGeneratesPage extends AppCompatActivity {


    private ImageView QRCodeImage;
    private ImageView PromoQRCodeImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_generates_page);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        QRCodeImage = findViewById(R.id.qr_code_image);
        PromoQRCodeImage = findViewById(R.id.promo_qr_code_image);

        if (MainActivity.isAddButtonClicked == true) {
            Integer eventCheckInId_int = new Random().nextInt(100000);
            String eventCheckInId = String.valueOf(eventCheckInId_int);

            Integer eventPromoId_int = new Random().nextInt(100000);
            String eventPromoId = String.valueOf(eventPromoId_int);

            generateQRCode(eventCheckInId);
            generatePromoQRCode(eventPromoId);
            if (!eventDataList.isEmpty()) {
                Event newEvent = eventDataList.get(eventDataList.size() - 1);
                newEvent.setQRCodeImage(QRCodeImage);
                newEvent.setPromoQRCodeImage(PromoQRCodeImage);
                newEvent.setEventCheckInId(eventCheckInId_int);
                newEvent.setEventPromoId(eventPromoId_int);
            }
        }

        MainActivity.isAddButtonClicked = false;

        QRCodeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavigationDrawerFragment.show(getSupportFragmentManager(), "Navigating");

                return true;
            }
        });

    }

        private void generateQRCode (String eventId){



            MultiFormatWriter writer = new MultiFormatWriter();
            try {

                BitMatrix matrix = writer.encode(eventId, BarcodeFormat.QR_CODE, 800, 800);

                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                QRCodeImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }

        private void generatePromoQRCode (String eventId){

            MultiFormatWriter writer = new MultiFormatWriter();
            try {
                BitMatrix matrix = writer.encode(eventId, BarcodeFormat.QR_CODE, 800, 800);

                BarcodeEncoder encoder = new BarcodeEncoder();
                Bitmap bitmap = encoder.createBitmap(matrix);
                PromoQRCodeImage.setImageBitmap(bitmap);
            } catch (WriterException e) {
                e.printStackTrace();
            }

        }




}

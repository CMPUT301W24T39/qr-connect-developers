package com.example.qrconnect;

import static com.example.qrconnect.MainActivity.eventDataList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

/**
 * The QRCodeGeneratesPage class that maintains the functions in the QRCodeGeneratesPage Activity.
 * It extends AppCompatAcitivty.
 */
public class QRCodeGeneratesPage extends AppCompatActivity {

    private ImageView QRCodeImage;
    private ImageView PromoQRCodeImage;
    private Bitmap bitMapQRCode;
    private Bitmap bitMapPromoQRCode;
    private ImageButton backButton1;
    private FirebaseFirestore db;

    /**
     * This defines the functions in the QRCodeGeneratesPage Activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_code_generates_page);

        backButton1 = findViewById(R.id.arrow_back_1);
        db = FirebaseFirestore.getInstance();

        QRCodeImage = findViewById(R.id.qr_code_image);
        PromoQRCodeImage = findViewById(R.id.promo_qr_code_image);

        String fieldNameCheckInQRCode = "checkInQRCodeImageUrl";
        String fieldNamePromoteQRCode = "promoQRCodeImageUrl";

        if (!eventDataList.isEmpty()) {

            Event newEvent = eventDataList.get(eventDataList.size() - 1);

            String eventCheckInId = newEvent.getEventId() + "_" + fieldNameCheckInQRCode + ".jpg";
            String eventPromoId = newEvent.getEventId() + "_" + fieldNamePromoteQRCode + ".jpg";
            generateQRCode(eventCheckInId);
            generatePromoQRCode(eventPromoId);

            newEvent.setQRCodeImage(QRCodeImage);
            newEvent.setPromoQRCodeImage(PromoQRCodeImage);

            QRCodeImage.setImageBitmap(bitMapQRCode);
            PromoQRCodeImage.setImageBitmap(bitMapPromoQRCode);

            uploadQRCodeAndUpdateEvent(bitMapQRCode, newEvent, fieldNameCheckInQRCode);
            uploadQRCodeAndUpdateEvent(bitMapPromoQRCode, newEvent, fieldNamePromoteQRCode);

        }

        QRCodeImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                BottomNavigationDrawerFragment bottomNavigationDrawerFragment = new BottomNavigationDrawerFragment();
                bottomNavigationDrawerFragment.show(getSupportFragmentManager(), "Navigating");

                return true;
            }
        });

        backButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    /**
     * This generates a QR code for check-in.
     * @param eventCheckInId This is a seed to generate a QR code.
     */
    private void generateQRCode (String eventCheckInId){

        MultiFormatWriter writer = new MultiFormatWriter();
        try {

            BitMatrix matrix = writer.encode(eventCheckInId, BarcodeFormat.QR_CODE, 800, 800);

            BarcodeEncoder encoder = new BarcodeEncoder();
            bitMapQRCode = encoder.createBitmap(matrix);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    /**
     * This generatss a promotion QR code that links to event description and event poster.
      * @param eventPromoId This is a seed to generate a promotion QR code.
     */
    private void generatePromoQRCode (String eventPromoId){

        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(eventPromoId, BarcodeFormat.QR_CODE, 800, 800);

            BarcodeEncoder encoder = new BarcodeEncoder();
            bitMapPromoQRCode = encoder.createBitmap(matrix);

        } catch (WriterException e) {
            e.printStackTrace();
        }

    }


    /**
     * This upload a QR code to firebase storage and update the URL for QR code in firebase database.
     * @param bitmap This is a pixel to draw.
     * @param event This is an Event object to be used to generate a unique QR code.
     * @param fieldName This is a reference in firebase database.
     */
    private void uploadQRCodeAndUpdateEvent(final Bitmap bitmap, final Event event, final String fieldName) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference qrCodeRef = storageRef.child("qrcodes/" + event.getEventId() + "_" + fieldName + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = qrCodeRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {

                Toast.makeText(QRCodeGeneratesPage.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                qrCodeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        String imageUrl = uri.toString();

                        db.collection("events")
                                .document(event.getEventId())
                                .update(fieldName, imageUrl)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(QRCodeGeneratesPage.this, "Event updated with QR image URL.", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(QRCodeGeneratesPage.this, "Fail to update the event with QR image URL.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
            }
        });
    }
}

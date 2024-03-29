package com.example.qrconnect;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.processing.SurfaceProcessorNode;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/*
https://www.youtube.com/watch?v=eSi28xqGjbE
 */
public class ShareQRCodeActivity extends AppCompatActivity {

    ImageButton backButton;
    ImageView qrCodeToShare;
    ImageView promoteQrCodeToShare;
    ImageButton share_button1;
    ImageButton share_button2;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_qr_code_page);

        qrCodeToShare = findViewById(R.id.qr_code_image_to_share);
        promoteQrCodeToShare = findViewById(R.id.promote_qr_code_image_to_share);
        share_button1 = findViewById(R.id.share_button);
        share_button2 = findViewById(R.id.share_button2);
        backButton = findViewById(R.id.arrow_back_3);
        Event currentEvent = (Event) getIntent().getSerializableExtra("EVENT");
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        if (currentEvent.getEventCheckInId() != null && currentEvent.getEventPromoId() != null){
            StorageReference qrCodeRef = storageRef.child("qrcodes/" + currentEvent.getEventCheckInId());
            StorageReference promoteQrCodeRef = storageRef.child("qrcodes/"+ currentEvent.getEventPromoId());

            loadEventQrCodeToShare(qrCodeRef, qrCodeToShare);
            loadEventQrCodeToShare(promoteQrCodeRef, promoteQrCodeToShare);
        }
        else{
            Toast.makeText(ShareQRCodeActivity.this, "Null QR Code" , Toast.LENGTH_SHORT).show();
        }
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        share_button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEvent.getEventCheckInId() != null){

                    shareImage(currentEvent.getEventCheckInId());
                }
                else {
                    Toast.makeText(ShareQRCodeActivity.this, "Share failed: Check-in id is null" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        share_button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentEvent.getEventCheckInId() != null){

                    shareImage(currentEvent.getEventPromoId());
                }
                else {
                    Toast.makeText(ShareQRCodeActivity.this, "Share failed: Promo id is null" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void shareImage(String id) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) qrCodeToShare.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Uri uri = getImageToShare(bitmap, id);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share Image"));


    }
    private Uri getImageToShare(Bitmap bitmap, String id) {
        File folder = new File(getCacheDir(), "qrcodes");
        Uri uri = null;
        try {
            folder.mkdirs();
            File file = new File(folder, id);
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this, "com.example.qrconnect.fileprovider", file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return uri;
    }


    private void loadEventQrCodeToShare(StorageReference qrCodeRef, ImageView qrCodeToShare) {
        qrCodeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(ShareQRCodeActivity.this)
                        .load(uri)
                        .into(qrCodeToShare);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e("EventDetailsActivity", "Error loading image: ", exception);
            }
        });
    }

}

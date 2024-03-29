package com.example.qrconnect;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import androidx.camera.core.Preview;
import androidx.lifecycle.LifecycleOwner;


import java.util.concurrent.ExecutionException;

/**
 * The QRCodeCheckInActivity class handles the QR code scanning.
 * It extends AppCompatActivity.
 */
public class QRCodeCheckInActivity extends AppCompatActivity {
    private static final String[] CAMERA_PERMISSION = new String[]{Manifest.permission.CAMERA};
    private static final int REQUEST_IMAGE_CAPTURE = 201;
    private static final int CAMERA_REQUEST_CODE = 10;

    private PreviewView previewView;
    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;


    /**
     * Called when the activity is first created. Responsible for initializing the activity.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}. Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_check_in);

        previewView = findViewById(R.id.camera_preview);
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);


        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED) {
            cameraProviderFuture.addListener(new Runnable() {
                @Override
                public void run() {
                    try {
                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                        // Set up preview
                        Preview preview = new Preview.Builder().build();
                        preview.setSurfaceProvider(previewView.getSurfaceProvider());
                        // Select back camera as a default
                        CameraSelector cameraSelector = new CameraSelector.Builder()
                                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                                .build();
                        cameraProvider.bindToLifecycle(QRCodeCheckInActivity.this, cameraSelector, preview);

                    } catch (ExecutionException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, ContextCompat.getMainExecutor(this));
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    CAMERA_PERMISSION,
                    CAMERA_REQUEST_CODE
            );
        }

    }


    /**
     * Called when an activity you launched exits, giving you the requestCode you started it with,
     * the resultCode is returned, and any other data from it.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Handle the camera image here
        }
    }
}
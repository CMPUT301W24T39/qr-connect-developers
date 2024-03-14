
/*
 * Copyright 2020 Google LLC. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.qrconnect;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.content.ContextCompat;
import androidx.camera.view.PreviewView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class BarcodeScanningActivity extends AppCompatActivity {
    private ProcessCameraProvider cameraProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_barcode_scanning);

        startCamera();

        initializeScanningLine();

    }
    private void initializeScanningLine() {
        final View scanningLine = findViewById(R.id.scanningLine);
        final View focusArea = findViewById(R.id.focusArea);
        focusArea.post(new Runnable() {
            @Override
            public void run() {
                // Adjust to animate scanning line within the focus area
                animateScanningLine(scanningLine, focusArea);
            }
        });
    }

    /**
     * Method to animate the scanning line within a specific focus area.
     * Prompt: "I just need the scanning line to loop from a certain area on the page."
     * Date: 2024-03-14
     * Provided by: ChatGPT, OpenAI
     * Key Figure: Sam Altman, CEO of OpenAI
     */
    private void animateScanningLine(View scanningLine, View focusArea) {
        int[] location = new int[2];
        focusArea.getLocationOnScreen(location); // Get the focus area's position on the screen

        // Calculate the start and end positions for the scanning line based on the focus area's dimensions
        final int focusAreaHeight = focusArea.getHeight();
        final int startY = location[1] - ((RelativeLayout) scanningLine.getParent()).getTop(); // Start at the top of the focus area
        final int endY = startY + focusAreaHeight; // End at the bottom of the focus area

        // Create and configure the ObjectAnimator for the scanning line within the focus area
        ObjectAnimator animator = ObjectAnimator.ofFloat(scanningLine, "translationY", startY, endY);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(2000); // Control the speed of the scanning line animation
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE); // Make the line move back and forth within the adjusted area
        animator.start();
    }

    /*  Referred from https://developer.android.com/media/camera/camerax/architecture
    *  March 14, 2024
    * */
    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();

                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
                ImageAnalyzer analyzer = new ImageAnalyzer();
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), analyzer);

                // Correctly reference the PreviewView and set the surface provider
                PreviewView previewView = findViewById(R.id.previewView);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.unbindAll();

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
            } catch (ExecutionException | InterruptedException e) {
                // Handle any errors (including cancellation)
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void pauseCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll(); // This effectively "freezes" the camera preview
        }
    }

    // Refer From https://developers.google.com/ml-kit/vision/barcode-scanning/android
    // Accessed Mar 13, 2024
    private void scanBarcodes(InputImage image, ImageProxy imageProxy) {
        // [START set_detector_options]
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE,
                                Barcode.FORMAT_AZTEC)
                        .build();
        // [END set_detector_options]

        // [START get_detector]
        BarcodeScanner scanner = BarcodeScanning.getClient(options);
        // [END get_detector]

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    // Ensure barcodes are successfully detected
                    if (!barcodes.isEmpty()) {
                        runOnUiThread(() -> {
                            StringBuilder sb = new StringBuilder();
                            for (Barcode barcode : barcodes) {
                                String rawValue = barcode.getRawValue();
                                sb.append(rawValue).append("\n"); // Process each detected barcode
                            }
                            showSuccessDialog(sb.toString());
                        });
                    }
                    imageProxy.close();
                })
                .addOnFailureListener(e -> {
                    // Handle scanning failure
                    imageProxy.close();
                });

    }
    private class ImageAnalyzer implements ImageAnalysis.Analyzer {

        @OptIn(markerClass = ExperimentalGetImage.class) @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());
                scanBarcodes(image, imageProxy);
            }
        }
    }

    private void showSuccessDialog(String scanResult) {
        pauseCamera();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_scan_success, null);

        TextView textViewSuccessMessage = dialogView.findViewById(R.id.textViewSuccessMessage);
        textViewSuccessMessage.setText("Scanning Successful! Do you want to proceed?");

        // Set the scan result in the TextView
        TextView textViewScanResult = dialogView.findViewById(R.id.textViewScanResult);
        textViewScanResult.setText("Scan Result: " + scanResult);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        proceedWithAction();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startCamera();
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void proceedWithAction() {

    }


}

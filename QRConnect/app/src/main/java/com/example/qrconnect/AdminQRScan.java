package com.example.qrconnect;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.CameraX;
import androidx.camera.core.ExperimentalGetImage;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.concurrent.ExecutionException;

/**
 * The ViewAsAdmin class manages the view as admin scan QR code page.
 * It extends AppCompatActivity.
 */
public class AdminQRScan extends AppCompatActivity {

    Button enter_token_button;
    private ProcessCameraProvider cameraProvider;
    private ObjectAnimator scanningLineAnimator;
    private String TAG = "AdminQRScanning";
    private boolean usingFrontCamera = false;
    private long lastActionTime = 0;// To prevent rapid multiple scans issue.
    private final String adminPassword = "1234";//Set the unique admin password.
    private View scanningLine;
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
        this.scanningLine = findViewById(R.id.admin_scanning_line);
        ImageButton backButton = findViewById(R.id.admin_qr_scan_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        startCamera();
        FloatingActionButton switchButton = findViewById(R.id.admin_switch_camera_button);
        switchButton.setOnClickListener(v -> switchCamera());
        enterTokenMethod();
    }

    /**
     * Prompt the users to enter token
     */
    private void enterTokenMethod(){
        enter_token_button = findViewById(R.id.admin_enter_token_button);
        enter_token_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseCamera();
                // Create and show a dialog to prompt the user to enter the admin password
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminQRScan.this);
                builder.setTitle("Enter Admin Password");

                final EditText input = new EditText(AdminQRScan.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String currentAdminPassword = input.getText().toString();
                        // Check if the entered password is correct
                        if (adminPassword.equals(currentAdminPassword)) {
                            // Start the AdminMenu activity
                            startActivity(new Intent(AdminQRScan.this, AdminMenu.class));
                            finish();
                        } else {
                            // Show an error message if the password is incorrect
                            Toast.makeText(AdminQRScan.this, "Incorrect admin password", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel(); // Cancel the dialog if the user clicks Cancel
                        finish();
                    }
                });
                builder.show(); // Show the dialog
            }
        });

    }

    /**
     * Initiates the camera with {@link CameraX} APIs. This method sets up the camera provider, selects the back camera,
     * and binds the lifecycle of the camera to the current activity.
     * Referred from https://developer.android.com/media/camera/camerax/architecture
     * March 14, 2024
     */
    private void startCamera() {
        startScanningLineAnimation();
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                Preview preview = new Preview.Builder().build();
                CameraSelector cameraSelector = usingFrontCamera ?
                        CameraSelector.DEFAULT_FRONT_CAMERA : CameraSelector.DEFAULT_BACK_CAMERA;
                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder().build();
                ImageAnalyzer analyzer = new ImageAnalyzer();
                imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this), analyzer);

                // Correctly reference the PreviewView and set the surface provider
                PreviewView previewView = findViewById(R.id.admin_camera_preview);
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                cameraProvider.unbindAll();

                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
            } catch (ExecutionException | InterruptedException e) {
                // Handle any errors (including cancellation)
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    /**
     * Animates the scanning line to move within the boundaries of the focus area. This method is inspired by
     * a prompt  "I need the scanning line to loop from a certain area on the page."
     * discussed on 2024-03-14 with ChatGPT, an AI developed by OpenAI, led by CEO Sam Altman. The
     * prompt was about creating a scanning line animation for a specific area on the screen.
     * <p>
     * This method calculates the start and end positions of the scanning line based on the focus area's dimensions
     * and uses an {@link ObjectAnimator} to animate the line back and forth, creating a visual effect of scanning.
     * </p>
     */
    private void startScanningLineAnimation() {
        int maxTranslationY = ((View) scanningLine.getParent()).getHeight() - scanningLine.getHeight();
        scanningLineAnimator = ObjectAnimator.ofFloat(scanningLine, View.TRANSLATION_Y, 0, maxTranslationY);
        scanningLineAnimator.setDuration(2000);
        scanningLineAnimator.setRepeatMode(ObjectAnimator.REVERSE);
        scanningLineAnimator.setRepeatCount(ObjectAnimator.INFINITE);
        scanningLineAnimator.setInterpolator(new LinearInterpolator());
        scanningLineAnimator.start();
    }
    /**
     * Pauses the camera preview by unbinding the camera provider from the current lifecycle.
     * This is used to temporarily halt the camera's operation.
     */
    private void pauseCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll(); // This effectively "freezes" the camera preview
        }
        if (scanningLineAnimator != null) {
            scanningLineAnimator.cancel();
        }
    }
    private class ImageAnalyzer implements ImageAnalysis.Analyzer {

        @OptIn(markerClass = ExperimentalGetImage.class) @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage,
                        imageProxy.getImageInfo().getRotationDegrees());
                scanBarcodes(image, imageProxy);
            }
        }
    }
    /**
     * Processes an image for barcode scanning. This method sets up a {@link BarcodeScanner} with the specified options,
     * analyzes the provided image, and upon success, extracts barcode information to proceed with further actions.
     * <p>
     * This implementation is based on guidelines and examples provided by the ML Kit for Barcode Scanning documentation.
     * For more details, visit: <a href="https://developers.google.com/ml-kit/vision/barcode-scanning/android">
     * ML Kit Barcode Scanning Guide</a>.
     * </p>
     * @param image The image to be scanned for barcodes.
     * @param imageProxy Proxy for accessing image data in a format suitable for barcode scanning.
     */
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
                            Log.d(TAG, "Scanning the QRCode successfully.");
                            proceedWithAction(sb.toString());
                        });
                    }
                    imageProxy.close();
                })
                .addOnFailureListener(e -> {
                    // Handle scanning failure
                    imageProxy.close();
                });

    }

    /**
     * Determines the appropriate action based on the scan result.
     * This could involve navigating to an admin explore page
     * or showing scanSuccessful dialog.
     *
     * @param scanResult The raw value obtained from scanning a barcode.
     */
    private void proceedWithAction(String scanResult) {
        pauseCamera();
        // the time delay mechanism is to prevent ``Debounce or Throttle Calls``
        // Referred from https://stackoverflow.com/questions/25991367/difference-between-throttling-and-debouncing-a-function
        // Donal, Sept 23, 2024.
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastActionTime < 1000){
            startCamera();
            return;
        }
        lastActionTime = currentTime;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Check if the scanResult matches the acceptableId
        if (scanResult.equals(adminPassword)) {
            pauseCamera();
            builder.setTitle("Alert");
            builder.setMessage("Scan Successful! This ID is accepted.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(AdminQRScan.this, AdminMenu.class));
                    startCamera();
                }
            });
            builder.show();
        } else {
            pauseCamera();
            builder.setTitle("Error");
            builder.setMessage("The Code is not accepted!");
            builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    startCamera();
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.show();
        }
    }

    /**
     * Change the camera on phone
     */
    private void switchCamera(){
        usingFrontCamera = !usingFrontCamera;
        pauseCamera();
        startCamera();
    }
}

package com.example.qrconnect;

import static android.app.PendingIntent.getActivity;
import static com.example.qrconnect.EventIdType.EVENT_CHECKIN;
import static com.example.qrconnect.EventIdType.EVENT_DETAILS;

import android.Manifest;


import static com.example.qrconnect.TimeConverter.stringToCalendar;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.camera.view.PreviewView;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
public class BarcodeScanningActivity extends AppCompatActivity {
    private ProcessCameraProvider cameraProvider;
    private String TAG = "BarcodeScanning";
    private boolean usingFrontCamera = false;
    private long lastActionTime = 0;// To prevent rapid multiple scans issue.
    private Event targetEvent;
    private final int MY_PERMISSIONS_REQUEST_LOCATION = 1;

    private LocationManager locationManager;
    private Location myLocation;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //private String currentUserId = UserPreferences.getUserId(getApplicationContext());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_scanning);
        startCamera();
        initializeScanningLine();
        setUpBackButton();
        ImageButton switchCameraButton = findViewById(R.id.switch_camera_button);
        switchCameraButton.setOnClickListener(v -> switchCamera());



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
     * Animates the scanning line to move within the boundaries of the focus area. This method is inspired by
     * a prompt  "I need the scanning line to loop from a certain area on the page."
     * discussed on 2024-03-14 with ChatGPT, an AI developed by OpenAI, led by CEO Sam Altman. The
     * prompt was about creating a scanning line animation for a specific area on the screen.
     * <p>
     * This method calculates the start and end positions of the scanning line based on the focus area's dimensions
     * and uses an {@link ObjectAnimator} to animate the line back and forth, creating a visual effect of scanning.
     * </p>
     *
     * @param scanningLine The scanning line view that needs to be animated.
     * @param focusArea    The focus area within which the scanning line will be animated.
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
    /**
     * Initiates the camera with {@link CameraX} APIs. This method sets up the camera provider, selects the back camera,
     * and binds the lifecycle of the camera to the current activity.
     * Referred from https://developer.android.com/media/camera/camerax/architecture
     * March 14, 2024
    */
    private void startCamera() {
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
    /**
     * Pauses the camera preview by unbinding the camera provider from the current lifecycle.
     * This is used to temporarily halt the camera's operation.
     */
    private void pauseCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll(); // This effectively "freezes" the camera preview
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
     * Defines behavior for analyzing images. This analyzer extracts {@link Image} data
     * from an {@link ImageProxy},
     * creates an {@link InputImage}, and initiates barcode scanning.
     */
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
     * Determines the appropriate action based on the scan result.
     * This could involve navigating to an event details page
     * or showing a check-in success dialog.
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

        isValidEventId(scanResult, new EventIdCallback() {
            @Override
            public void onEventIdValidated(boolean isValid, String qrCodeIdentifier,
                                           EventIdType eventType) {
                if (isValid) {
                    switch (eventType) {
                        case EVENT_DETAILS:
                            Log.d(TAG, "Valid event ID: " + qrCodeIdentifier);
                            Intent intent = new Intent(BarcodeScanningActivity.this,
                                    PromoDetailsActivity.class);
                            intent.putExtra("EVENT_ID", qrCodeIdentifier);
                            startActivity(intent);
                            startCamera();
                            break;
                        case EVENT_CHECKIN:
                            readEventFromDatabase(qrCodeIdentifier);
                            break;
                        default:
                            showFailureDialog("Scanned, but eventId issue");
                            break;
                    }
                } else {
                    showFailureDialog("Not Valid Code (not exists)");
                }
            }

            @Override
            public void onError(Exception e) {
                handleQueryError();
            }
        });
    }

    private void readEventFromDatabase(String eventId) {
        DocumentReference eventRef = db.collection("events").document(eventId);
        eventRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        try{
                            targetEvent = createEventFromDocumentSnapshot(documentSnapshot, eventId);
                            showSuccessDialog();
                        } catch (Exception ex){
                            showFailureDialog(ex.getMessage());
                        }
                    } else {
                        showFailureDialog("Event not found");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error reading event from database", e);
                    handleQueryError();
                });
    }

    /**
     * Displays a dialog indicating a successful scan with options to confirm or reject the result.
     */
    private void showSuccessDialog() {
        pauseCamera();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_scan_success, null);

        TextView textViewSuccessMessage = dialogView.findViewById(R.id.textViewSuccessMessage);
        textViewSuccessMessage.setText("Scanning Successful! Do you want to Check-in?");

        // Set the scan result in the TextView
        TextView textViewScanResult = dialogView.findViewById(R.id.textViewScanResult);
        textViewScanResult.setText("Event: " + targetEvent.getEventTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        proceedWithConfirmAction();
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

    private void proceedWithConfirmAction() {
        DocumentReference eventRef = db.collection("events")
                .document(targetEvent.getEventId());
        String currentUserId = UserPreferences.getUserId(getApplicationContext());

        DocumentReference userRef = db.collection("users").document(currentUserId);

        userRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        String currentUserName = firstName + " " + lastName;

                        //get current uesr location
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                                || ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                            // Permissions are granted, proceed to get the location
                            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
                                @Override
                                public void onLocationChanged(Location location) {
                                    // Use the location object here
                                    String latitude = Double.toString(location.getLatitude());
                                    String longitude = Double.toString(location.getLongitude());

                                    String checkInLocation = latitude + " " + longitude;
                                    targetEvent.addAttendee(currentUserId, currentUserName, checkInLocation);
                                    System.out.println("locationis3 " + checkInLocation);
                                    locationManager.removeUpdates(this); // Stop receiving updates immediately
                                }
                            });
                        } else {
                            // Permissions are not granted, request permissions
                            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.ACCESS_FINE_LOCATION }, MY_PERMISSIONS_REQUEST_LOCATION);

                            // Inform the user that location access is required
                            Toast.makeText(getApplicationContext(), "First enable LOCATION ACCESS", Toast.LENGTH_LONG).show();

                        }

                        Log.d(TAG, "User's name: " + currentUserName);
                        updateEventAttendeeLists(eventRef);

                    } else {
                        Log.d(TAG, "User document does not exist");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching user document", e);
                });
    }



    private void updateEventAttendeeLists(DocumentReference eventRef) {
        eventRef.update("attendeeListIdToTimes", targetEvent.getAttendeeListIdToCheckInTimes())
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "Event updated attendeeListIdToTimes successfully");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating event attendeeListIdToTimes", e);
                });

        eventRef.update("attendeeListIdToName", targetEvent.getAttendeeListIdToName())
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "Event updated attendeeListIdToName successfully");
                    showCheckInSuccessfullyDialog();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating event attendeeListIdToName", e);
                });

        eventRef.update("attendeeListIdToLocations", targetEvent.getAttendeeListIdToCheckInLocations())
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "Event updated attendeeListIdToLocations successfully");
                    showCheckInSuccessfullyDialog();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating event attendeeListIdToName", e);
                });

        eventRef.update("currentAttendance", targetEvent.getAttendeeListIdToName().size())
                .addOnSuccessListener(v -> {
                    Log.d(TAG, "Event updated currentAttendance successfully");
                    showCheckInSuccessfullyDialog();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error updating event currentAttendance", e);
                });
    }


    private void showCheckInSuccessfullyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check-In Successful");
        builder.setMessage("Check-in was successful. What would you like to do next?");
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Resume using the camera
                startCamera();
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void showFailureDialog(String errorMsg) {
        pauseCamera();
        handleQRCodeNotFound(errorMsg);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Error");
        builder.setMessage(errorMsg);
        builder.setPositiveButton("Retry", (dialog, which) -> {
            startCamera();
        });

        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Validates whether the scanned QR code corresponds to a valid event ID in the database.
     *
     * @param scanResult The raw value obtained from scanning a barcode.
     * @param callback   The callback to be invoked with the validation result.
     */
    private void isValidEventId(String scanResult, EventIdCallback callback){

        String qrCodeIdentifier = scanResult.split("_")[0];
        String qrCodeType = scanResult.split("_")[1];
        EventIdType eventType = determineEventType(qrCodeType); // Determine event type

        db.collection("events").whereEqualTo("eventId", qrCodeIdentifier)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            callback.onEventIdValidated(true, qrCodeIdentifier, eventType);
                        } else {
                            callback.onEventIdValidated(false, qrCodeIdentifier,
                                    EventIdType.UNKNOWN);
                        }
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }

    /**
     * Handles the scenario when a QR code is not found in the database. This method logs the event and
     * displays a toast message to the user indicating the QR code could not be located.
     */
    private void handleQRCodeNotFound(String msg) {
        Log.e(TAG, "Proceed QR code error " + msg);
        Toast.makeText(this, "QR Code not found in the database. "+msg,
                Toast.LENGTH_LONG).show();
    }

    /**
     * Handles errors that occur during database queries. This method logs the error and
     * displays a toast message to the user indicating an issue with querying the database.
     */

    private void handleQueryError() {
        Log.e(TAG, "Error querying the database");
        Toast.makeText(this, "Error querying the database.", Toast.LENGTH_LONG).show();
    }

    /**
     * Interface definition for a callback to be invoked when an event ID has been validated.
     * This allows for asynchronous validation results to be processed accordingly.
     */

    interface EventIdCallback {
        void onEventIdValidated(boolean isValid, String qrCodeIdentifier, EventIdType eventIdType);
        void onError(Exception e);
    }

    /**
     * Determines the type of event based on the scanned result. This method checks if the scan result
     * contains specific keywords to categorize the event as either details or check-in.
     *
     * @param scanResult The raw scan result from the QR code.
     * @return The type of event identified from the scan result.
     */
    public EventIdType determineEventType(String scanResult) {

        if (scanResult.contains("promo")) {
            return EVENT_DETAILS;
        } else if (scanResult.contains("checkIn")) {
            return EVENT_CHECKIN;
        }
        return EventIdType.UNKNOWN;
    }

    /**
     * Sets up the back button with a click listener that finishes the current activity.
     * This method finds the back button in the layout and assigns a click listener to it,
     * allowing users to return to the previous screen.
     */
    private void setUpBackButton(){
        ImageButton backButton = findViewById(R.id.scanning_back_nv_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void switchCamera(){
        usingFrontCamera = !usingFrontCamera;
        pauseCamera();
        startCamera();
    }

    private Event createEventFromDocumentSnapshot(DocumentSnapshot documentSnapshot,
                                                  String eventId) {
        // Extract data from the document snapshot
        String eventTitle = documentSnapshot.getString("title");
        String dateString = documentSnapshot.getString("date");
        Calendar date = DateConverter.stringToCalendar(dateString);
        String timeString = documentSnapshot.getString("time");
        Calendar time = TimeConverter.stringToCalendar(timeString);
        String location = documentSnapshot.getString("location");
        Long capacityLong = documentSnapshot.getLong("capacity");
        Integer capacity = capacityLong != null ? capacityLong.intValue() : 0;
        String announcement = documentSnapshot.getString("announcement");
        String checkInQRCodeImageUrl = documentSnapshot.getString("checkInQRCodeImageUrl");
        String promoQRCodeImageUrl = documentSnapshot.getString("promoQRCodeImageUrl");
        String hostId = documentSnapshot.getString("hostId");
        HashMap<String, Long> attendeeListIdToTimes =
                (HashMap<String, Long>) documentSnapshot.get("attendeeListIdToTimes");
        HashMap<String, String> attendeeListIdToName =
                (HashMap<String, String>) documentSnapshot.get("attendeeListIdToName");
        HashMap<String, String> attendeeListIdToLocations =
                (HashMap<String, String>) documentSnapshot.get("attendeeListIdToLocations");
        // Create the Event object manually
        return new Event(eventTitle, date, time, location, capacity, announcement,
                checkInQRCodeImageUrl, promoQRCodeImageUrl, eventId, hostId,
                attendeeListIdToTimes, attendeeListIdToName, attendeeListIdToLocations);
    }

}

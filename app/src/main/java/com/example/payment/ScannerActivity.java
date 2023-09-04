package com.example.payment;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.camera.core.ExperimentalGetImage;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
@OptIn(markerClass = androidx.camera.core.ExperimentalGetImage.class)
public class ScannerActivity extends AppCompatActivity {

    private PreviewView previewView;
    private ExecutorService cameraExecutor;

    LottieAnimationView cam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        previewView = findViewById(R.id.previewView);
        cameraExecutor = Executors.newSingleThreadExecutor();
      //  cam = findViewById(R.id.lottie);
        startCamera();

     /*   new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(),ScannerActivity.class);
                startActivity(i);
            }
        },5000); */
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1280, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build();

        imageAnalysis.setAnalyzer(cameraExecutor, this::processImage);

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis);
    }

    private void processImage(@ExperimentalGetImage ImageProxy image) {
        Image mediaImage = image.getImage();
        if (mediaImage != null) {
            InputImage inputImage = InputImage.fromMediaImage(mediaImage, image.getImageInfo().getRotationDegrees());

            BarcodeScanner scanner = BarcodeScanning.getClient();
            Task<List<Barcode>> result = scanner.process(inputImage)
                    .addOnSuccessListener(barcodes -> {
                        for (Barcode barcode : barcodes) {
                            if (barcode.getFormat() == Barcode.FORMAT_QR_CODE) {
                                String qrCodeValue = barcode.getRawValue();
                                runOnUiThread(() -> {
                                    //Toast.makeText(ScannerActivity.this, "QR Code: " + qrCodeValue, Toast.LENGTH_SHORT).show();
                                    sreachNumber(qrCodeValue);

                                });
                            }
                        }
                    })
                    .addOnFailureListener(e -> {
                        e.printStackTrace();
                    })
                    .addOnCompleteListener(task -> image.close());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
    }


    private void sreachNumber (String number)
    {
        String phoneNumberToSearch = number;

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("phoneNumbers")
                .document(phoneNumberToSearch)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String uid = document.getString("uid");

                                // Now you have the UID associated with the phone number
                                // You can proceed to fetch email and phone number from the "users" collection
                                fetchUserDataFromUsersCollection(uid);
                            } else {
                                // Handle case when phone number doesn't exist
                                //Log.d(TAG, "Phone number not found.");
                                Toast.makeText(ScannerActivity.this, "User Not Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Handle Firestore read failure
                           // Log.e(TAG, "Error getting document", task.getException());
                            Toast.makeText(ScannerActivity.this,"\"Error getting document\"task.getException()", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void fetchUserDataFromUsersCollection(String uid) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // Store user data in variables
                                String userEmail = document.getString("email");
                                String userPhoneNumber = document.getString("phoneNumber");

                                // Now you have the user's email and phone number stored in variables
                                Log.d(TAG, "User's email: " + userEmail);
                                Log.d(TAG, "User's phone number: " + userPhoneNumber);
                                 Intent i = new Intent(ScannerActivity.this,TransActivity.class);
                                i.putExtra("uuid",uid);
                                startActivity(i);

                                // Do whatever you want with the user's data here
                            } else {
                                // Handle case when user document doesn't exist
                                Log.d(TAG, "User document not found.");
                            }
                        } else {
                            // Handle Firestore read failure
                            Log.e(TAG, "Error getting document", task.getException());
                        }
                    }
                });
    }


}
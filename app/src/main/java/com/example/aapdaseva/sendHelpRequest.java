package com.example.aapdaseva;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class sendHelpRequest extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    Button send;
    EditText edtMessage;
    ImageButton btnCamera;
    ImageView capturedImage;

    ImageButton locationAccessButton;

    FirebaseFirestore db;
    String currentPhotoPath;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_help_request);

        db = FirebaseFirestore.getInstance(); // Initialize Firestore
        btnCamera = findViewById(R.id.btnCamera);
        send = findViewById(R.id.buttonSend);
        edtMessage = findViewById(R.id.editTextText);
        capturedImage = findViewById(R.id.capturedImage);
        locationAccessButton=findViewById(R.id.btnlocation);
        String Email = getIntent().getStringExtra("email");

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });

        locationAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the message from the EditText
                String message = edtMessage.getText().toString().trim();

                Map<String, String> chat = new HashMap<>();

                chat.put("requests", message);
                chat.put("senderID", Email);
                chat.put("imagePath", currentPhotoPath); // Save the image path

                // Check if the message is not empty
                if (!message.isEmpty()) {
                    // Create a Firestore document and store the message and image path as fields
                    db.collection("helpRequest")
                            .add(chat)
                            .addOnSuccessListener(documentReference -> {
                                // Document added successfully
                                Toast.makeText(sendHelpRequest.this, "Request sent successfully.", Toast.LENGTH_SHORT).show();
                                edtMessage.setText(""); // Clear the EditText after sending
                                capturedImage.setImageBitmap(null); // Clear the captured image

                            })
                            .addOnFailureListener(e -> {
                                // Error adding document
                                Toast.makeText(sendHelpRequest.this, "Failed to send request. Please try again.", Toast.LENGTH_SHORT).show();
                            });
                } else {
                    Toast.makeText(sendHelpRequest.this, "Please enter a message.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Display the captured image on the ImageView
            setPic();
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = capturedImage.getWidth();
        int targetH = capturedImage.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        capturedImage.setImageBitmap(bitmap);
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastKnownLocation(new RegistrationActivity.LocationCallback() {
                @Override
                public void onLocationReceived(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        Log.d("LocationInfo", "Latitude: " + latitude + ", Longitude: " + longitude);

                        fetchAddressFromCoordinates(latitude, longitude);



                        // Now you have the latitude and longitude, you can use them as needed
                        // For example, you can store them in Firestore along with other agency registration data
                    } else {
                        Toast.makeText(getApplicationContext(), "Location data is not available", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    private void getLastKnownLocation(final RegistrationActivity.LocationCallback callback) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            callback.onLocationReceived(location);
                        }
                    });
        }
    }

    private interface LocationCallback {
        void onLocationReceived(Location location);
    }

    private void fetchAddressFromCoordinates(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);
                String addressText = "";

                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressText += address.getAddressLine(i);
                    if (i < address.getMaxAddressLineIndex()) {
                        addressText += "\n";
                    }
                }

                edtMessage.setText("My current Location" + addressText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the IOException, such as showing an error message to the user.
        }
    }
}

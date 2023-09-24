package com.example.aapdaseva;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.location.Location;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private EditText editTextAgencyName;
    private EditText editTextContactPerson;
    private EditText editTextEmail;
    private EditText editTextPhone;
    private EditText editTextAddress;
    private Spinner spinnerAreaOfExpertise;
    private EditText editTextPassword;
    private Button buttonRegister;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ImageButton locationAccessButton = findViewById(R.id.locationAccessButton);

        locationAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestLocationPermission();
            }
        });




        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        editTextAgencyName = findViewById(R.id.editTextAgencyName);
        editTextContactPerson = findViewById(R.id.editTextContactPerson);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextAddress = findViewById(R.id.editTextAddress);
        spinnerAreaOfExpertise = findViewById(R.id.editTextAreaOfExpertise);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        String[] areas = new String[]{"Medical", "Firefighting", "Rescue", "Engineering"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, areas);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAreaOfExpertise.setAdapter(spinnerArrayAdapter);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String agencyName = editTextAgencyName.getText().toString().trim();
                final String contactPerson = editTextContactPerson.getText().toString().trim();
                final String email = editTextEmail.getText().toString().trim();
                final String phone = editTextPhone.getText().toString().trim();
                final String address = editTextAddress.getText().toString().trim();
                final String areaOfExpertise = spinnerAreaOfExpertise.getSelectedItem().toString();
                final String password = editTextPassword.getText().toString().trim();

                if (agencyName.isEmpty() || contactPerson.isEmpty() || email.isEmpty() ||
                        phone.isEmpty() || address.isEmpty() || areaOfExpertise.isEmpty() ||
                        password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        getLastKnownLocation(new LocationCallback() {
                                            @Override
                                            public void onLocationReceived(Location location) {
                                                if (location != null) {
                                                    double latitude = location.getLatitude();
                                                    double longitude = location.getLongitude();

                                                    // Create a new agency document in Firestore
                                                    Map<String, Object> agency = new HashMap<>();
                                                    agency.put("agencyname", agencyName);
                                                    agency.put("contactperson", contactPerson);
                                                    agency.put("email", email);
                                                    agency.put("agencynumber", phone);
                                                    agency.put("address", address);
                                                    agency.put("area", areaOfExpertise);
                                                    agency.put("usertype", "agency");
                                                    agency.put("password", password);
                                                    agency.put("latitude", latitude); // Add latitude
                                                    agency.put("longitude", longitude); // Add longitude

                                                    // Add the agency to Firestore
                                                    db.collection("Users")
                                                            .add(agency)
                                                            .addOnSuccessListener(documentReference -> {
                                                                Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                                            })
                                                            .addOnFailureListener(e -> {
                                                                Toast.makeText(RegistrationActivity.this, "Failed to register. Try again.", Toast.LENGTH_SHORT).show();
                                                            });
                                                    //direct open the dashboarrd page
                                                    SharedPreferences sharedPreferences = getSharedPreferences("registartion", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                                    editor.putBoolean("registartion", false);
                                                    editor.apply();

                                                    Intent intent = new Intent(getApplicationContext(), AgencyLoginActivity.class);
                                                    startActivity(intent);
                                                    finish();




                                                } else {
                                                    Toast.makeText(RegistrationActivity.this, "Location data is not available", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {
                                        if (task.getException() != null) {
                                            Log.e("FirebaseAuth", task.getException().getMessage());
                                            Toast.makeText(RegistrationActivity.this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private void requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getLastKnownLocation(new LocationCallback() {
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
                        Toast.makeText(RegistrationActivity.this, "Location data is not available", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
    }

    private void getLastKnownLocation(final LocationCallback callback) {
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

    interface LocationCallback {
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

                editTextAddress.setText(addressText);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the IOException, such as showing an error message to the user.
        }
    }
}
